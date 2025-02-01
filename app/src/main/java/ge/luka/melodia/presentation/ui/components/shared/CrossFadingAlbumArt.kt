package ge.luka.melodia.presentation.ui.components.shared

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import ge.luka.melodia.R

enum class ErrorPainterType {
    PLACEHOLDER, SOLID_COLOR
}

@Composable
fun CrossFadingAlbumArt(
    modifier: Modifier,
    containerModifier: Modifier = Modifier,
    artUri: String,
    errorPainterType: ErrorPainterType,
    contentScale: ContentScale = ContentScale.Crop
) {

    val context = LocalContext.current
    val imageRequest = remember(artUri) {
        ImageRequest.Builder(context)
            .data(artUri)
            .size(Size.ORIGINAL).build()
    }

    var firstPainter by remember {
        mutableStateOf<Painter>(ColorPainter(Color.Black))
    }

    val solidColorPainter = remember { ColorPainter(Color.Black) }
    val placeholderPainter = painterResource(id = R.drawable.ic_albums)

    rememberAsyncImagePainter(
        model = imageRequest,
        contentScale = ContentScale.Crop,
        onState = {
            when (it) {
                is AsyncImagePainter.State.Success -> {
                    val newPainter = it.painter
                    firstPainter = newPainter
                }

                is AsyncImagePainter.State.Error -> {
                    firstPainter =
                        if (errorPainterType == ErrorPainterType.PLACEHOLDER) placeholderPainter
                        else solidColorPainter
                }

                else -> {}
            }
        }
    )

    Crossfade(modifier = containerModifier, targetState = firstPainter, label = "") {
        Image(
            modifier = modifier,
            painter = it,
            contentDescription = null,
            contentScale = contentScale
        )
    }
}
