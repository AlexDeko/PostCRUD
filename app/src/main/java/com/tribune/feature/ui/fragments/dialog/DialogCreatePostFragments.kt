package com.tribune.feature.ui.fragments.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.tribune.ARG_IMAGE_ID
import com.tribune.ARG_POST_ID
import com.tribune.R
import com.tribune.ARG_TOKEN_FIREBASE
import com.tribune.component.creator.creteNewPost
import com.tribune.component.network.isNetworkConnect
import com.tribune.component.notification.NotificationHelper
import com.tribune.core.api.MediaApi
import com.tribune.core.api.PostsApi
import com.tribune.core.api.ProfileApi
import com.tribune.core.utils.toast
import com.tribune.feature.data.dto.PostResponseDto
import kotlinx.android.synthetic.main.fragment_diolog.*
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.android.ext.android.get
import java.io.ByteArrayOutputStream

class DialogCreatePostFragments : DialogFragment() {

    private val REQUEST_IMAGE_CAPTURE = 1
    private var imageBitmap: Bitmap? = null
    private var imageId: String? = null
    private val media: MediaApi = get()
    private val posts: PostsApi = get()
    private val users: ProfileApi = get()
    private val token = arguments?.getString(ARG_TOKEN_FIREBASE) ?: ""



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return AlertDialog.Builder(requireContext())
            .setView(R.layout.dialog_create_post)
            .setCancelable(false)
            .show()
    }


    private fun showDialog(context: Context) {


        val dialog = AlertDialog.Builder(context)
            .setView(R.layout.dialog_create_post)
            .setCancelable(false)
            .show()

        with(dialog) {
            createPostButton.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                val user = users.getProfile()
                val createPost: PostResponseDto = creteNewPost(
                    contentText = textPostInput.text.toString(), author = user.username,
                    ownerId = user.id, imageId = imageId
                )

                    val post = posts.createPost(createPost, tokenFireBase = token)

                    arguments = Bundle().apply {
                        putString(ARG_IMAGE_ID, imageId)
                        putLong(ARG_POST_ID, post.id) }
                }
                hide()
            }
            cancelButton.setOnClickListener {
                cancel()
            }
            addPhotoButton.setOnClickListener {

                dispatchTakePictureIntent(context)
                addPhotoButton.isEnabled = false
            }
        }
    }

    private fun dispatchTakePictureIntent(context: Context) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(context.packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {
            imageBitmap = data?.extras?.get("data") as Bitmap
            pushMediaImage(imageBitmap!!)
        }
    }

    private fun pushMediaImage(bitmap: Bitmap) = viewLifecycleOwner.lifecycleScope.launch {
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val reqFIle = RequestBody.create("image/jpeg".toMediaTypeOrNull(), bos.toByteArray())
        val body = MultipartBody.Part.createFormData("file", "image.jpg", reqFIle)
        try {
            val mediaImage = media.setMediaPost(body)
            imageId = mediaImage.id
            NotificationHelper.mediaUploaded(mediaImage, requireContext())
        } catch (e: Exception) {
            networkError(e)
        }

    }


    private fun networkError(e: Exception) {
        if (!isNetworkConnect(requireContext())) errorNoNetwork.visibility = View.VISIBLE
        else toast(e.localizedMessage!!)
    }
}