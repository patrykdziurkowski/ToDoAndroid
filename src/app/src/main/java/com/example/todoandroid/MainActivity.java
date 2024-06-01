package com.example.todoandroid;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.Toast;

import com.example.todoandroid.ui.TasksViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.todoandroid.databinding.ActivityMainBinding;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private TasksViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(TasksViewModel.class);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_tasks,
                R.id.navigation_details,
                R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED) {
            scheduleDeadlineNotifications();
        } else {
            requestPermissions(new String[] { "android.permission.POST_NOTIFICATIONS" }, Constants.REQUEST_POST_NOTIFICATIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != Constants.REQUEST_POST_NOTIFICATIONS) return;
        if (grantResults.length > 0
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            scheduleDeadlineNotifications();
        }
    }

    private void scheduleDeadlineNotifications() {
        List<Task> tasks = viewModel.getTasks().getValue();
        for (Task task : tasks) {
            scheduleNotification(task);
        }
    }

    private void scheduleNotification(Task task) {
        if (task.getDeadline() == null) { return; }

        Calendar calendar = Calendar.getInstance();
        Date alarmDate = (task.getDeadline().before(new Date())) ? new Date() : task.getDeadline();
        calendar.setTime(alarmDate);
        calendar.setTimeInMillis(calendar.getTimeInMillis() + 3000);
        Intent intent = new Intent(getApplicationContext(), Notification.class);
        intent.putExtra("title", task.getTitle());
        intent.putExtra("text", String.format("Due: %s", task.getDeadline()));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(),
                Constants.PENDING_NOTIFICATION_ID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}