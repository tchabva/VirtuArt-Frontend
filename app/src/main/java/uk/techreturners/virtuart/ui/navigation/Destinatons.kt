package uk.techreturners.virtuart.ui.navigation

import kotlinx.serialization.Serializable

// Tabs are the sub navigation graphs
@Serializable
sealed interface Tabs {
    @Serializable
    data object Artworks : Tabs

    @Serializable
    data object Search : Tabs

    @Serializable
    data object Exhibitions : Tabs

    @Serializable
    data object Profile : Tabs
}

// For each Composable Screen
@Serializable
sealed interface Screens {
    @Serializable
    data object Artworks : Screens

    @Serializable
    data object Search : Screens

    @Serializable
    data object Exhibitions : Screens

    @Serializable
    data object Profile : Screens

    @Serializable
    data class ViewArtwork(val artworkId: String) : Screens

    @Serializable
    data class ExhibitionDetail(val exhibitionId: String) : Screens

    companion object {
        val screensWithoutBottomNav = listOf(
            ViewArtwork::class,
            ExhibitionDetail::class
        )

        val screensWithTopAppBar = listOf(
            ViewArtwork::class,
            ExhibitionDetail::class
        )
    }
}