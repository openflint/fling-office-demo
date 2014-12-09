package com.infthink.myflingoffice;

import org.json.JSONException;
import org.json.JSONObject;

import tv.matchstick.flint.Flint;
import tv.matchstick.flint.FlintDevice;
import tv.matchstick.flint.FlintManager;
import tv.matchstick.flint.ResultCallback;
import tv.matchstick.flint.Status;
import android.util.Log;

public abstract class OfficeChannel implements Flint.MessageReceivedCallback {
    private static final String TAG = OfficeChannel.class.getSimpleName();

    private static final String OFFICE_NAMESPACE = "urn:flint:com.infthink.demo.office";

    // Commands
    private static final String KEY_COMMAND = "command";
    private static final String KEY_SHOW = "show";
    private static final String KEY_PAGE = "page";
    private static final String KEY_LEAVE = "leave";

    private static final String KEY_FILE = "file";
    private static final String KEY_ACT_CMD = "cmd";

    protected OfficeChannel() {
    }

    /**
     * Returns the namespace for this fling channel.
     */
    public String getNamespace() {
        return OFFICE_NAMESPACE;
    }

    public final void show(FlintManager apiClient, String filePath) {
        try {
            Log.d(TAG, "show: " + filePath);
            JSONObject payload = new JSONObject();
            payload.put(KEY_COMMAND, KEY_SHOW);
            payload.put(KEY_FILE, filePath);
            sendMessage(apiClient, payload.toString());
        } catch (JSONException e) {
            Log.e(TAG, "Cannot create object to show file", e);
        }
    }

    public final void act(FlintManager apiClient, final int cmd) {
        Log.d(TAG, "act: cmd:" + cmd);
        try {
            JSONObject payload = new JSONObject();
            payload.put(KEY_COMMAND, KEY_PAGE);
            payload.put(KEY_ACT_CMD, cmd);
            sendMessage(apiClient, payload.toString());
        } catch (JSONException e) {
            Log.e(TAG, "Cannot create object to send a action", e);
        }
    }

    /**
     * Sends a command to leave the current game.
     */
    public final void leave(FlintManager apiClient) {
        try {
            Log.d(TAG, "leave");
            JSONObject payload = new JSONObject();
            payload.put(KEY_COMMAND, KEY_LEAVE);
            sendMessage(apiClient, payload.toString());
        } catch (JSONException e) {
            Log.e(TAG, "Cannot create object to leave", e);
        }
    }

    @Override
    public void onMessageReceived(FlintDevice flingDevice, String namespace,
            String message) {
        Log.d(TAG, "onTextMessageReceived: " + message);

    }

    private final void sendMessage(FlintManager apiClient, String message) {
        Log.d(TAG, "Sending message: (ns=" + OFFICE_NAMESPACE + ") " + message);
        Flint.FlintApi.sendMessage(apiClient, OFFICE_NAMESPACE, message)
                .setResultCallback(new SendMessageResultCallback(message));
    }

    private final class SendMessageResultCallback implements
            ResultCallback<Status> {
        String mMessage;

        SendMessageResultCallback(String message) {
            mMessage = message;
        }

        @Override
        public void onResult(Status result) {
            if (!result.isSuccess()) {
                Log.d(TAG,
                        "Failed to send message. statusCode: "
                                + result.getStatusCode() + " message: "
                                + mMessage);
            }
        }
    }

}
