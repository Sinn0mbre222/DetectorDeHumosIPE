package com.fireAlert;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.fireAlert.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Se llama cuando llega un mensaje y la app está en primer plano,
     * o si es una notificación de datos.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Verificar si el mensaje contiene datos
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Payload de datos del mensaje: " + remoteMessage.getData());
        }

        // Verificar si el mensaje contiene una carga útil de notificación
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Cuerpo de la notificación: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
    }

    /**
     * Se llama si el token de instancia se actualiza. Esto puede ocurrir si la seguridad de
     * el token anterior se ha visto comprometida.
     */
    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Nuevo token generado: " + token);
        // Aquí deberías enviar este token a tu servidor Express para guardarlo en tu base de datos
        // y saber a quién enviarle notificaciones luego.
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implementar llamada a tu API para guardar el token del usuario
    }

    /**
     * Crea y muestra una notificación simple que contiene el mensaje recibido de FCM.
     */
    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // PendingIntent inmutable es requerido en versiones nuevas de Android
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        String channelId = "mi_canal_notificaciones_default";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(android.R.drawable.ic_dialog_info) // Asegúrate de tener un icono válido
                        .setContentTitle(title != null ? title : "Notificación")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Desde Android Oreo (API 26), se requiere un canal de notificación.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Notificaciones Express",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }
}