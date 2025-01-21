package com.metalsensor.gold.detector.finder.ui.frament

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.hardware.Camera
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.common.util.concurrent.ListenableFuture
import com.metalsensor.gold.detector.finder.R
import com.metalsensor.gold.detector.finder.databinding.FragmentScanBinding
import com.metalsensor.gold.detector.finder.dialog.Saving_dialog
import com.metalsensor.gold.detector.finder.dialog.dialogCoin
import com.metalsensor.gold.detector.finder.dialog.dialogmang
import com.metalsensor.gold.detector.finder.manager.FlashlightManager
import com.metalsensor.gold.detector.finder.model.EventMessage
import com.metalsensor.gold.detector.finder.model.EventMessage2
import com.metalsensor.gold.detector.finder.ui.AboutCoinActivity
import com.metalsensor.gold.detector.finder.ui.CoinrecognitionActivity
import com.metalsensor.gold.detector.finder.ui.ResultActivity
import com.metalsensor.gold.detector.finder.utils.AboutCoinViewmodel
import com.metalsensor.gold.detector.finder.utils.CoinViewModel
import com.metalsensor.gold.detector.finder.utils.Const
import com.metalsensor.gold.detector.finder.utils.Const._checkFlashGold
import com.metalsensor.gold.detector.finder.utils.Const.sampleImages
import com.metalsensor.gold.detector.finder.utils.InternetConnectionChecker
import com.metalsensor.gold.detector.finder.utils.gone
import com.metalsensor.gold.detector.finder.utils.onSingleClick
import com.metalsensor.gold.detector.finder.utils.tap
import com.metalsensor.gold.detector.finder.utils.visible
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.features2d.BFMatcher
import org.opencv.features2d.ORB
import org.opencv.imgproc.Imgproc
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.ExecutorService

class ScanFragment : Fragment() {
    private lateinit var intent: Intent
    private var isClick = true
    private val handel by lazy {
        Handler(Looper.getMainLooper())
    }
    private var checkflass = false
    private lateinit var flashlightManager: FlashlightManager
    var TAG = "SCANCOIN"
    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!
    private var selectedImageMat: Mat? = null

    private val imageProcessor = ImageProcessor()
    private val coinViewModel: CoinViewModel by viewModels()

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
        private const val CAMERA_REQUEST = 2
    }

    private var imageCapture: ImageCapture? = null
    private var savingDialog: Saving_dialog? = null
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var internetChecker: InternetConnectionChecker

    private val loadingDialog by lazy {
        dialogmang(requireContext())
    }
    private val loadingCoin by lazy {
        dialogCoin(requireContext())
    }
    private val aboutcoinViewModel: AboutCoinViewmodel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        flashlightManager = FlashlightManager(requireContext())
        initSampleImages()
        setupButtons()

        internetChecker = InternetConnectionChecker(requireContext())
    }

    private fun showSavingDialog() {
        savingDialog = Saving_dialog(requireContext(), R.style.SavingDialog, getString(R.string.save))
        savingDialog?.setCancelable(false)
        savingDialog?.setCanceledOnTouchOutside(false)
        savingDialog?.show()
    }

    private fun hideSavingDialog() {
        if (savingDialog != null && savingDialog?.isShowing == true) {
            savingDialog?.dismiss()
        }
    }

    private fun setupButtons() {
        binding.imvcmron.tap {
            checkflass = false
            binding.imvcmron.gone()
            binding.imvcmroff.visible()
        }
        binding.imvcmroff.tap {
            checkflass = true
            binding.imvcmron.visible()
            binding.imvcmroff.gone()
        }
        binding.choseimage.onSingleClick {
            openGallery()
        }
        startCameraX()
        binding.chup.tap {
            binding.chup.isEnabled=false
                if (a == 0) {
                    if (checkflass) {
                        imageCapture?.flashMode = ImageCapture.FLASH_MODE_ON
                    }
                    captureAndSaveImage()
                }else{
                    binding.chup.isEnabled=true
                    loadingDialog.show()
                }

        }
    }

    private fun captureAndSaveImage() {
        val imageCapture = this.imageCapture
        if (imageCapture == null) {
            Toast.makeText(requireContext(), "Camera is not ready yet", Toast.LENGTH_SHORT).show()
            return
        }
        val photoFile =
            File(requireContext().cacheDir, "captured_image_${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    if (checkflass) {
                        imageCapture.flashMode = ImageCapture.FLASH_MODE_OFF
                    }

                    hideSavingDialog()

                    val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                    binding.selectedImageView.setImageBitmap(bitmap)
                    binding.viewFinder.visibility = View.GONE
                    binding.selectedImageView.visible()
                    val uri = Uri.fromFile(photoFile)
                    processSelectedImage(uri)
                }

                override fun onError(exception: ImageCaptureException) {
                    binding.chup.isEnabled=true
                    // Reset flash mode on error
                    imageCapture.flashMode = ImageCapture.FLASH_MODE_OFF
                    Toast.makeText(
                        requireContext(),
                        "Capture failed: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }

    private fun startCameraX() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build()
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                imageCapture = ImageCapture.Builder().build()

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )

                preview.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                binding.chup.isEnabled = true

            } catch (e: Exception) {
                Log.e("CameraX", "Use case binding failed", e)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun processSelectedImage(uri: Uri) {
        showSavingDialog()
        Log.d(TAG, "vao 1")
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val inputStream = context?.contentResolver?.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()

                selectedImageMat = Mat()
                Log.d(TAG, "processSelectedImage: " + selectedImageMat)
                Utils.bitmapToMat(bitmap, selectedImageMat)
                Imgproc.cvtColor(selectedImageMat, selectedImageMat, Imgproc.COLOR_BGR2GRAY)

                binding.selectedImageView.setImageBitmap(bitmap)
                compareSelectedImage()
            } catch (e: Exception) {
                Log.e("CoinRecognition", "Error processing image: ${e.message}")
            }
        }
    }
    private suspend fun compareSelectedImage() {
        withContext(Dispatchers.Default) {
            try {
                selectedImageMat?.let { selected ->
                    try {
                        Log.d(TAG, "Selected image is being compared.")

                        val resizedSelectedImage = try {
                            if (selected.width() > 161 || selected.height() > 163) {
                                val resizedMat = Mat()
                                Imgproc.resize(selected, resizedMat, Size(161.0, 163.0))
                                resizedMat
                            } else {
                                selected
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Error resizing image: ${e.message}", e)
                            selected
                        }

                        var bestMatch: Pair<Const.CoinImage, Double>? = null

                        for (sample in sampleImages) {
                            try {
                                if (sample.mat.type() != selected.type()) {
                                    Log.d(TAG, "Image type mismatch for sample ${sample.id}")
                                }
                                val similarity = imageProcessor.compareImages(sample.mat, resizedSelectedImage)
                                Log.d(TAG, "Compared with sample ${sample.id}, similarity: $similarity")

                                if (bestMatch == null || similarity > bestMatch.second) {
                                    bestMatch = sample to similarity
                                }
                            } catch (e: Exception) {
                                Log.e(TAG, "Error comparing with sample ${sample.id}: ${e.message}", e)
                                continue
                            }
                        }

                        withContext(Dispatchers.Main) {
                            try {
                                bestMatch?.let { (sample, similarity) ->
                                    val confidencePercentage = similarity * 100
//
                                    if (confidencePercentage < 20.0) {
                                        Log.d(TAG, "compareSelectedImage: "+confidencePercentage)
                                        Log.d(TAG, "No match found. Confidence is too low: ${String.format("%.1f%%", confidencePercentage)}")
                                        loadingCoin.show()
                                        hideSavingDialog()
                                        binding.selectedImageView.gone()
                                        binding.viewFinder.visible()
                                        binding.chup.isEnabled=true
                                    } else {
                                        Log.d(TAG, "Best match found: ${sample.id}, Confidence: ${String.format("%.1f%%", confidencePercentage)}")
                                        Const.id = sample.id
                                        Const.Title = sample.name
                                        Const.url = sample.url
                                        Const.url2 = sample.url2
                                        lifecycleScope.launch {
                                            try {
                                                aboutcoinViewModel.fetchCoinDetails()
                                            } catch (e: Exception) {
                                                Log.e(TAG, "Error fetching coin details: ${e.message}", e)
                                                Toast.makeText(context, "Error fetching coin details", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        check()
                                        hideSavingDialog()
                                        about()
                                    }
                                } ?: run {
                                    loadingCoin.show()
                                    hideSavingDialog()
                                    binding.selectedImageView.gone()
                                    binding.viewFinder.visible()
                                    Log.d(TAG, "No matching sample found.")
                                    hideSavingDialog()
                                }
                            } catch (e: Exception) {
                                hideSavingDialog()
                                binding.selectedImageView.gone()
                                binding.viewFinder.visible()
                                Log.e(TAG, "Error processing results: ${e.message}", e)
                                Toast.makeText(context, "Error processing results", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: Exception) {
                        EventBus.getDefault().post(EventMessage2("true"))
                    }
                } ?: Log.e(TAG, "Selected image is null.")
            } catch (e: Exception) {
                Log.e(TAG, "Fatal error in compareSelectedImage: ${e.message}", e)
                withContext(Dispatchers.Main) {

                    loadingCoin.show()
                    hideSavingDialog()
                    binding.selectedImageView.gone()
                    binding.viewFinder.visible()
                    Log.d(TAG, "No matching sample found.2")
                    initSampleImages()
                }
            } finally {
                hideSavingDialog()
            }
        }
    }


    private class ImageProcessor {
        fun compareImages(sample: Mat, query: Mat): Double {
            val orb = ORB.create()
            val keypointsSample = MatOfKeyPoint()
            val keypointsQuery = MatOfKeyPoint()
            val descriptorsSample = Mat()
            val descriptorsQuery = Mat()

            orb.detectAndCompute(sample, Mat(), keypointsSample, descriptorsSample)
            orb.detectAndCompute(query, Mat(), keypointsQuery, descriptorsQuery)

            val matcher = BFMatcher.create(Core.NORM_HAMMING, true)
            val matches = MatOfDMatch()
            matcher.match(descriptorsSample, descriptorsQuery, matches)

            return matches.toList()
                .filter { it.distance < 50.0 }
                .size.toDouble() / matches.rows()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return

        when (requestCode) {
            PICK_IMAGE_REQUEST -> {
                data?.data?.let { processSelectedImage(it) }
            }
        }
    }

    private fun initSampleImages() {
        showSavingDialog()
        observeViewModel()
    }

    private var loadedImageCount = 0

    private fun loadSampleImage(id: Int, url: String, ur2: String, name: String, onComplete: () -> Unit) {
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val mat = Mat()
                    Utils.bitmapToMat(resource, mat)
                    Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY)
                    sampleImages.add(Const.CoinImage(id, mat, url, ur2, name))
//                    Log.d(TAG, "Image loaded: $name")
                    onComplete() // Gọi callback khi xử lý xong ảnh
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    Log.d(TAG, "Image loading cleared for: $url")
                    onComplete() // Gọi callback nếu ảnh không tải được
                }
            })
    }


    var coinName: String = ""
    var reverseImage: String = ""
    var obverseImage: String = ""
    private fun observeViewModel() {
        coinViewModel.coinList.observe(viewLifecycleOwner) { fetchedCoinList ->
            if (!fetchedCoinList.isNullOrEmpty()) {
                loadedImageCount = 0 // Đặt lại biến đếm
                val totalCoins = fetchedCoinList.size // Tổng số đồng xu cần xử lý

                for (coin in fetchedCoinList) {
                     coinName = coin.name ?: continue
                     obverseImage = coin.obverseImage ?: continue
                     reverseImage = coin.reverseImage ?: continue

                    loadSampleImage(coin.id, obverseImage, reverseImage, coinName) {
                        loadedImageCount++
//                        Log.d(TAG, "Image loaded: $loadedImageCount/$totalCoins")

                        if (loadedImageCount == totalCoins-10) {
                            onAllImagesLoaded()
                        }
                    }
                }

            } else {
                Log.e("CoinlibraryFragment", "No coins fetched.")
            }
        }
    }

    // Hàm xử lý sau khi tất cả ảnh đã được tải và lưu
    private fun onAllImagesLoaded() {
      hideSavingDialog()
    }


    private fun about() {
        binding.chup.isEnabled=true
        binding.selectedImageView.gone()
        binding.viewFinder.visible()
        if (a == 0) {
            isClick = true
            intent = Intent(requireContext(), ResultActivity::class.java)
            startActivity(intent)
            handel.postDelayed({ isClick = true }, 500)
        }else{
            binding.chup.isEnabled=true
            loadingDialog.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

    override fun onDestroy() {
        super.onDestroy()
    }
    private var a=0
    private fun check() {
        lifecycleScope.launch {
            internetChecker.connectionState.collect { state ->
                when (state) {
                    is InternetConnectionChecker.ConnectionState.Unknown -> {
                        a = 1
                    }

                    is InternetConnectionChecker.ConnectionState.NoConnection -> {
                        a = 1
                    }

                    is InternetConnectionChecker.ConnectionState.HasConnection -> {
                        a = 1
                    }

                    is InternetConnectionChecker.ConnectionState.HasInternet -> {
                        a = 0
                    }
                    else -> {}
                }
                Log.d("TAG", "check: "+a)
            }
        }
        internetChecker.startPeriodicCheck()
    }

}