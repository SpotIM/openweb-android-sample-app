package openweb.sample.ui.screens.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.koin.compose.koinInject
import openweb.sample.R
import openweb.sample.data.local.OWArticleSettingsProvider
import openweb.sample.ui.screens.about.AboutScreen
import openweb.sample.ui.screens.verticals.model.VerticalMockData
import openweb.sample.ui.screens.verticals.screens.ArticleVerticalScreen
import openweb.sample.ui.screens.verticals.screens.SiderailVerticalScreen
import openweb.sample.ui.screens.verticals.screens.SportVerticalScreen
import openweb.sample.ui.screens.verticals.screens.VideoVerticalScreen
import openweb.sample.ui.screens.verticals.theme.financeColor
import openweb.sample.ui.screens.verticals.theme.newsColor
import openweb.sample.ui.screens.verticals.theme.recipesColor
import openweb.sample.ui.screens.verticals.theme.sideRailColor
import openweb.sample.ui.screens.verticals.theme.sportsColor
import openweb.sample.ui.screens.verticals.theme.videoColor

private object HomeScreenDimensions {
    val PaddingMedium = 12.dp
    val PaddingLarge = 16.dp

    val FontSizeSection = 12.sp
    val FontSizeCardTitle = 18.sp
    val FontSizeCardDescription = 13.sp
    val FontSizeIcon = 28.sp

    val CardHeight = 180.dp
    val IconContainerSize = 56.dp
    val CardCornerRadius = 16.dp
    val IconContainerCornerRadius = 12.dp
    val CardElevation = 2.dp

    val LineHeightCardDescription = 18.sp
}

private val VERTICALS = listOf(
    VerticalCard(
        id = VerticalRoutes.NEWS,
        icon = "📰",
        title = "News",
        color = newsColor
    ),
    VerticalCard(
        id = VerticalRoutes.FINANCE,
        icon = "📈",
        title = "Finance",
        color = financeColor
    ),
    VerticalCard(
        id = VerticalRoutes.RECIPES,
        icon = "🍲",
        title = "Recipes",
        color = recipesColor
    ),
    VerticalCard(
        id = VerticalRoutes.SPORT,
        icon = "⚽",
        title = "Sport",
        color = sportsColor
    ),
    VerticalCard(
        id = VerticalRoutes.VIDEO,
        icon = "▶️",
        title = "Video",
        color = videoColor
    ),
    VerticalCard(
        id = VerticalRoutes.SIDERAIL,
        icon = "📄",
        title = "Side Rail",
        color = sideRailColor
    )
)

private object SlideAnimations {
    private const val DURATION_MS = 300

    val enterTransition = slideInHorizontally(
        initialOffsetX = { it },
        animationSpec = tween(DURATION_MS, easing = FastOutSlowInEasing)
    ) + fadeIn(animationSpec = tween(DURATION_MS, easing = FastOutSlowInEasing))

    val exitTransition = slideOutHorizontally(
        targetOffsetX = { -it },
        animationSpec = tween(DURATION_MS, easing = FastOutSlowInEasing)
    ) + fadeOut(animationSpec = tween(DURATION_MS, easing = FastOutSlowInEasing))

    val popEnterTransition = slideInHorizontally(
        initialOffsetX = { -it },
        animationSpec = tween(DURATION_MS, easing = FastOutSlowInEasing)
    ) + fadeIn(animationSpec = tween(DURATION_MS, easing = FastOutSlowInEasing))

    val popExitTransition = slideOutHorizontally(
        targetOffsetX = { it },
        animationSpec = tween(DURATION_MS, easing = FastOutSlowInEasing)
    ) + fadeOut(animationSpec = tween(DURATION_MS, easing = FastOutSlowInEasing))
}

/**
 * Main navigation setup using standard Navigation Compose
 */
@UnstableApi
@Composable
fun VerticalNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onNavigateToSettings: () -> Unit = {}
) {
    val articleSettingsProvider: OWArticleSettingsProvider = koinInject()

    NavHost(
        navController = navController,
        startDestination = VerticalRoutes.HOME,
        modifier = modifier
    ) {
        composable(VerticalRoutes.HOME) {
            VerticalHomeScreen(
                onCardClick = { vertical -> navController.navigate(vertical.id) },
                onAboutClick = { navController.navigate(VerticalRoutes.ABOUT) }
            )
        }

        composable(
            route = VerticalRoutes.NEWS,
            enterTransition = { SlideAnimations.enterTransition },
            exitTransition = { SlideAnimations.exitTransition },
            popEnterTransition = { SlideAnimations.popEnterTransition },
            popExitTransition = { SlideAnimations.popExitTransition }
        ) {
            ArticleVerticalScreen(
                mockData = VerticalMockData.News(articleSettingsProvider),
                onBackClick = { navController.popBackStack() },
                onSettingsClick = onNavigateToSettings
            )
        }

        composable(
            route = VerticalRoutes.FINANCE,
            enterTransition = { SlideAnimations.enterTransition },
            exitTransition = { SlideAnimations.exitTransition },
            popEnterTransition = { SlideAnimations.popEnterTransition },
            popExitTransition = { SlideAnimations.popExitTransition }
        ) {
            ArticleVerticalScreen(
                mockData = VerticalMockData.Finance(articleSettingsProvider),
                onBackClick = { navController.popBackStack() },
                onSettingsClick = onNavigateToSettings
            )
        }

        composable(
            route = VerticalRoutes.RECIPES,
            enterTransition = { SlideAnimations.enterTransition },
            exitTransition = { SlideAnimations.exitTransition },
            popEnterTransition = { SlideAnimations.popEnterTransition },
            popExitTransition = { SlideAnimations.popExitTransition }
        ) {
            ArticleVerticalScreen(
                mockData = VerticalMockData.Recipes(articleSettingsProvider),
                onBackClick = { navController.popBackStack() },
                onSettingsClick = onNavigateToSettings
            )
        }

        composable(
            route = VerticalRoutes.SPORT,
            enterTransition = { SlideAnimations.enterTransition },
            exitTransition = { SlideAnimations.exitTransition },
            popEnterTransition = { SlideAnimations.popEnterTransition },
            popExitTransition = { SlideAnimations.popExitTransition }
        ) {
            SportVerticalScreen(
                mockData = VerticalMockData.Sport(articleSettingsProvider),
                onBackClick = { navController.popBackStack() },
                onSettingsClick = onNavigateToSettings
            )
        }

        composable(
            route = VerticalRoutes.VIDEO,
            enterTransition = { SlideAnimations.enterTransition },
            exitTransition = { SlideAnimations.exitTransition },
            popEnterTransition = { SlideAnimations.popEnterTransition },
            popExitTransition = { SlideAnimations.popExitTransition }
        ) {
            VideoVerticalScreen(
                mockData = VerticalMockData.Video(articleSettingsProvider),
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = VerticalRoutes.SIDERAIL,
            enterTransition = { SlideAnimations.enterTransition },
            exitTransition = { SlideAnimations.exitTransition },
            popEnterTransition = { SlideAnimations.popEnterTransition },
            popExitTransition = { SlideAnimations.popExitTransition }
        ) {
            SiderailVerticalScreen(
                mockData = VerticalMockData.SideRail(articleSettingsProvider),
                onBackClick = { navController.popBackStack() },
                onSettingsClick = onNavigateToSettings
            )
        }

        composable(
            route = VerticalRoutes.ABOUT,
            enterTransition = { SlideAnimations.enterTransition },
            exitTransition = { SlideAnimations.exitTransition },
            popEnterTransition = { SlideAnimations.popEnterTransition },
            popExitTransition = { SlideAnimations.popExitTransition }
        ) {
            AboutScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

@Composable
private fun HomeToolbar(onAboutClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.openweb_logo),
            contentDescription = stringResource(R.string.openweb_logo_content_description),
            modifier = Modifier.size(40.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        ) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = stringResource(R.string.home_screen_description),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                fontSize = 12.sp,
                overflow = TextOverflow.Ellipsis
            )
        }

        IconButton(onClick = onAboutClick) {
            Icon(
                painter = painterResource(R.drawable.ic_info_toolbar),
                contentDescription = stringResource(R.string.about_button_content_description),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerticalHomeScreen(
    onCardClick: (VerticalCard) -> Unit = {},
    onAboutClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val verticalsWithDescriptions = VERTICALS.map { card ->
        val descriptionResId = when (card.id) {
            "news" -> R.string.vertical_news_description
            "finance" -> R.string.vertical_finance_description
            "recipes" -> R.string.vertical_recipes_description
            "sport" -> R.string.vertical_sport_description
            "video" -> R.string.vertical_video_description
            "siderail" -> R.string.vertical_siderail_description
            else -> throw IllegalArgumentException("Unknown card id: ${card.id}")
        }
        card.copy(description = stringResource(descriptionResId))
    }

    Scaffold(
        topBar = { HomeToolbar(onAboutClick = onAboutClick) },
        modifier = modifier
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(
                start = HomeScreenDimensions.PaddingLarge,
                end = HomeScreenDimensions.PaddingLarge,
                top = paddingValues.calculateTopPadding() + HomeScreenDimensions.PaddingLarge,
                bottom = HomeScreenDimensions.PaddingLarge
            ),
            horizontalArrangement = Arrangement.spacedBy(HomeScreenDimensions.PaddingMedium),
            verticalArrangement = Arrangement.spacedBy(HomeScreenDimensions.PaddingMedium)
        ) {
            item(span = { GridItemSpan(2) }) {
                Text(
                    text = stringResource(R.string.choose_vertical_section_title),
                    fontSize = HomeScreenDimensions.FontSizeSection,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            items(verticalsWithDescriptions) { vertical ->
                VerticalCardItem(
                    vertical = vertical,
                    onClick = { onCardClick(vertical) }
                )
            }
        }
    }
}

@Composable
fun VerticalCardItem(
    vertical: VerticalCard,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(HomeScreenDimensions.CardHeight)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(HomeScreenDimensions.CardCornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = HomeScreenDimensions.CardElevation
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(HomeScreenDimensions.PaddingLarge),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = RoundedCornerShape(HomeScreenDimensions.IconContainerCornerRadius),
                color = vertical.color.copy(alpha = 0.15f),
                modifier = Modifier.size(HomeScreenDimensions.IconContainerSize)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = vertical.icon,
                        fontSize = HomeScreenDimensions.FontSizeIcon
                    )
                }
            }

            Spacer(modifier = Modifier.height(HomeScreenDimensions.PaddingMedium))

            Text(
                text = vertical.title,
                fontSize = HomeScreenDimensions.FontSizeCardTitle,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = vertical.description,
                fontSize = HomeScreenDimensions.FontSizeCardDescription,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = HomeScreenDimensions.LineHeightCardDescription,
                textAlign = TextAlign.Center
            )
        }
    }
}
