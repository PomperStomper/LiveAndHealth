package ru.pankratov.trofimov.liveandhealth.controls

import android.content.Context
import android.net.Uri
import android.widget.Toast

import android.widget.VideoView
import java.lang.Exception


open class VideoViewUtils {

    fun playRawVideo(context: Context, videoView: VideoView, resName: String?) {
        try {
            // ID of video file.
            val id: Int? = resName?.let { getRawResIdByName(context, it) }
            val uri: Uri =
                Uri.parse("android.resource://" + context.packageName.toString() + "/" + id)
//            Log.i(LOG_TAG, "Video URI: $uri")
            Toast.makeText(context, "Play Raw Video!", Toast.LENGTH_SHORT).show()
            videoView.setVideoURI(uri)
            videoView.requestFocus()
        } catch (e: Exception) {
//            Log.e(LOG_TAG, "Error Play Raw Video: " + e.message)
            Toast.makeText(context, "Error Play Raw Video: " + e.message, Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }
//    fun onResume() {
//        val video = findViewById(R.id.videoview1) as VideoView
//        video.setOnPreparedListener { mp -> mp.isLooping = true }
//        val videoPath =
//            Uri.parse("android.resource://" + getPackageName().toString() + "/" + R.raw.great)
//        video.setVideoURI(videoPath)
//        video.start()
//    }

    fun playLocalVideo(context: Context?, videoView: VideoView?, localPath: String?) {
        try {
        } catch (e: Exception) {
//            Log.e(LOG_TAG, "Error Play Local Video: " + e.message)
            Toast.makeText(context, "Error Play Local Video: " + e.message, Toast.LENGTH_SHORT)
                .show()
            e.printStackTrace()
        }
    }

    fun playURLVideo(context: Context?, videoView: VideoView, videoURL: String) {
        try {
//            Log.i(LOG_TAG, "Video URL: $videoURL")
            val uri: Uri = Uri.parse(videoURL)
            videoView.setVideoURI(uri)
            videoView.requestFocus()
        } catch (e: Exception) {
//            Log.e(LOG_TAG, "Error Play URL Video: " + e.message)
            Toast.makeText(context, "Error Play URL Video: " + e.message, Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    fun getRawResIdByName(context: Context, resName: String): Int {
        val pkgName: String = context.packageName
        // Return 0 if not found.
        val resID: Int = context.resources.getIdentifier(resName, "raw", pkgName)
//        Log.i(LOG_TAG, "Res Name: $resName==> Res ID = $resID")
        return resID
    }

    companion object {
        val RAW_VIDEO_SAMPLE = "watervideo"
        val LOCAL_VIDEO_SAMPLE = "/storage/emulated/0/DCIM/Camera/VID_20180212_195520.mp4"
        val URL_VIDEO_SAMPLE = "https://ex1.o7planning.com/_testdatas_/mov_bbb.mp4"

        val LOG_TAG = "AndroidVideoView"
    }
}