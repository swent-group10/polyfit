package com.github.se.polyfit.ui.screen

import android.annotation.SuppressLint
import androidx.annotation.FloatRange
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.github.se.polyfit.R
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

// version 1
val lipsum = "\n" +
    "What is Lorem Ipsum?\n" +
    "\n" +
    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n" +
    "Why do we use it?\n" +
    "\n" +
    "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).\n" +
    "\n" +
    "Where does it come from?\n" +
    "\n" +
    "Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of \"de Finibus Bonorum et Malorum\" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", comes from a line in section 1.10.32."

@Preview
@Composable
fun Carousel() {
  // List of image resources
  val images = listOf(
    R.drawable.food1,
    R.drawable.food2,
    R.drawable.food3,
    R.drawable.food1,
    R.drawable.food2,
    R.drawable.food3,
    R.drawable.food1,
    R.drawable.food2,
    R.drawable.food3,
  )

  Row(modifier = Modifier
                  .fillMaxSize()
                  .horizontalScroll(rememberScrollState()),
    horizontalArrangement = Arrangement.SpaceBetween) {


    for (image in images) {
      Card(modifier = Modifier.width(400.dp)
                          .padding(16.dp)
                          .verticalScroll(rememberScrollState())) {
        Image(
          painter = painterResource(id = image),
          contentDescription = null,
          contentScale = ContentScale.Fit,
          modifier = Modifier.clip(CardDefaults.shape)
        )

        Text(lipsum)
      }
    }

  }
}


// Padding values
private val cardPadding = 25.dp
private val imagePadding = 10.dp

// Shadow and shape values for the card
private val shadowElevation = 15.dp
private val borderRadius = 15.dp
private val shape = RoundedCornerShape(borderRadius)

// Offset for the parallax effect
private val xOffset = cardPadding.value * 2

@Preview
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ParallaxCarousel() {
  // Get screen dimensions and density
  val screenWidth = LocalConfiguration.current.screenWidthDp.dp
  val screenHeight = LocalConfiguration.current.screenHeightDp.dp
  val density = LocalDensity.current.density

  // List of image resources
  val images = listOf(
    R.drawable.food1,
    R.drawable.food2,
    R.drawable.food3,
    R.drawable.food1,
    R.drawable.food2,
    R.drawable.food3,
    R.drawable.food1,
    R.drawable.food2,
    R.drawable.food3,
  )

  // Create a pager state
  val pagerState = rememberPagerState {
    images.size
  }

  // Calculate the height for the pager
  val pagerHeight = screenHeight / 1.5f

  // HorizontalPager composable: Swiping through images
  HorizontalPager(
    state = pagerState,
    modifier = Modifier
      .fillMaxWidth()
      .height(pagerHeight),
  ) { page ->
    // Calculate the parallax offset for the current page
    val parallaxOffset = pagerState.getOffsetFractionForPage(page) * screenWidth.value

    // Call ParallaxCarouselItem with image resource and parameters
    ParallaxCarouselItem(
      images[page],
      parallaxOffset,
      pagerHeight,
      screenWidth,
      density
    )
  }
}

@Composable
fun ParallaxCarouselItem(
  imageResId: Int,
  parallaxOffset: Float,
  pagerHeight: Dp,
  screenWidth: Dp,
  density: Float,
) {
  // Load the image bitmap
  val imageBitmap = ImageBitmap.imageResource(id = imageResId)

  // Calculate the draw size for the image
  val drawSize = imageBitmap.calculateDrawSize(density, screenWidth, pagerHeight)

  // Card composable for the item
  Card(
    modifier = Modifier
      .fillMaxSize()
      .padding(cardPadding)
      .background(Color.White, shape)
      .shadow(elevation = shadowElevation, shape = shape)
  ) {
    // Canvas for drawing the image with parallax effect
    Canvas(
      modifier = Modifier
        .fillMaxSize()
        .padding(imagePadding)
        .clip(shape)
    ) {
      // Translate the canvas for parallax effect
      translate(left = parallaxOffset * density) {
        // Draw the image
        drawImage(
          image = imageBitmap,
          srcSize = IntSize(imageBitmap.width, imageBitmap.height),
          dstOffset = IntOffset(-xOffset.toIntPx(density), 0),
          dstSize = drawSize,
        )
      }
    }
  }
}

// Function to calculate draw size for the image
private fun ImageBitmap.calculateDrawSize(density: Float, screenWidth: Dp, pagerHeight: Dp): IntSize {
  val originalImageWidth = width / density
  val originalImageHeight = height / density

  val frameAspectRatio = screenWidth / pagerHeight
  val imageAspectRatio = originalImageWidth / originalImageHeight

  val drawWidth = xOffset + if (frameAspectRatio > imageAspectRatio) {
    screenWidth.value
  } else {
    pagerHeight.value * imageAspectRatio
  }

  val drawHeight = if (frameAspectRatio > imageAspectRatio) {
    screenWidth.value / imageAspectRatio
  } else {
    pagerHeight.value
  }

  return IntSize(drawWidth.toIntPx(density), drawHeight.toIntPx(density))
}

// Extension function to convert Float to Int in pixels
private fun Float.toIntPx(density: Float) = (this * density).roundToInt()



// test 2
@Preview
@Composable
fun asd(){
  ComposableCarousal(
    carouselItems = listOf(
      R.drawable.food1,
      R.drawable.food2,
      R.drawable.food3,
      R.drawable.food1,
      R.drawable.food2,
      R.drawable.food3,
      R.drawable.food1,
      R.drawable.food2,
      R.drawable.food3,
    ),
    showDots = true,
    delayMillis = 3000L
  )

}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ComposableCarousal(
  carouselItems: List<Int>,
  showDots: Boolean = true,
  delayMillis: Long = 3000L
) {
  var selectedItemIndex by remember { mutableStateOf(0) }

  //val coroutineScope = rememberCoroutineScope()

  LaunchedEffect(selectedItemIndex) {
    // Auto-change the image every 'delayMillis' milliseconds
    while (true) {
      delay(delayMillis)
      selectedItemIndex = (selectedItemIndex + 1) % carouselItems.size
    }
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    content = {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(bottom = 8.dp), // Remove top and right padding, keep bottom padding
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        ImageCarousel(
          carouselItems = carouselItems,
          selectedItemIndex = selectedItemIndex,
          onSelectedItemChange = { index ->
            selectedItemIndex = index
          }
        )
        if (showDots) {
          Spacer(modifier = Modifier.height(8.dp))
          DotsIndicator(
            numDots = carouselItems.size,
            currentIndex = selectedItemIndex,
            onDotClick = { index ->
              selectedItemIndex = index
            }
          )
        }
      }
    }
  )
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ImageCarousel(
  carouselItems: List<Int>,
  selectedItemIndex: Int,
  onSelectedItemChange: (Int) -> Unit
) {
  AnimatedVisibility(
    visible = selectedItemIndex >= 0, // To prevent animation on first item when recomposing
    enter = slideInHorizontally(
      initialOffsetX = { 1000 },
      animationSpec = tween(durationMillis = 1000)
    ),
    exit = slideOutHorizontally(
      targetOffsetX = { -1000 },
      animationSpec = tween(durationMillis = 1000)
    )
  ) {
    Crossfade(targetState = carouselItems[selectedItemIndex]) { imageRes ->
      Image(
        painter = painterResource(imageRes),
        contentDescription = null,
        modifier = Modifier
          .fillMaxWidth() // Take full width available
          .height(320.dp) // Take full height available
          .graphicsLayer {
            // Add any transformation effects you desire
          },
        contentScale = ContentScale.FillBounds, // Image takes full width and height of the Box
      )
    }
  }
}

@Composable
fun DotsIndicator(
  numDots: Int,
  currentIndex: Int,
  onDotClick: (Int) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(8.dp),
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically
  ) {
    for (i in 0 until numDots) {
      Dot(index = i, isSelected = i == currentIndex, onDotClick)
      Spacer(modifier = Modifier.width(8.dp)) // Add space between dots
    }
  }
}

@Composable
fun Dot(
  index: Int,
  isSelected: Boolean,
  onDotClick: (Int) -> Unit
) {
  val color = if (isSelected) Color.LightGray else Color.Gray
  Box(
    modifier = Modifier
      .size(12.dp) // Increase the size of dots for better visibility
      .background(color = color, shape = CircleShape)
      .clickable {
        onDotClick(index)
      }
  )
}