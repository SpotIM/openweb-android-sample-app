# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.5.0] - 2026-03-31
### New Features
- Introduced the Components API, a new unified API combining Views and Flows capabilities with simplified integration through three
  functions: getPreConversation, getConversation, and openConversation.
- Added support for customizable report success messages via server-side text overrides.
### Bug Fixes
- Prevented comments from being posted after a conversation was closed by the admin.
- Restored User Mentions functionality.
- Restored the ability to appeal comments.
- Fixed an issue where conversations failed to load for certain posts until refreshed.
- Fixed conversation displaying as empty after logging in via Comment Creation.
- Fixed Facebook links posted via webview not being reflected in the conversation.
- Fixed the "X" button to switch to root comment disappearing in certain cases in Comment Creation.
- Corrected Comment Creation layout in Expanded mode after redirecting back from the login flow.
- Fixed Comment Creation restoring to an incorrect edit/reply state when returning from background.
- Fixed draft comment disappearing when opening Comment Creation from Pre-Conversation.
- Fixed auto-capitalization not applying to the first letter of a new sentence after a period.
- Corrected navigation when tapping an avatar in Pre-Conversation; it now opens the expected screen instead of Full Conversation.
- Fixed "Authorize to participate" button navigating to Full Conversation instead of the login screen.
- Corrected missing indent line for comments with a moderation status.
- Fixed sorting name color not reflecting customization settings.
- Corrected the success message shown after reporting a comment.
- Fixed sorting changes in Full Conversation not updating Pre-Conversation when navigating back.
### Improvements
- Aligned report reason language options to match the web platform.
### Customer-Specific Fixes
- Resolved crashes related to network error handling and authentication flows. [Philstar, MarketWatch]

## [2.4.3] - 2025-03-16
- Hide 'Components' SDK api from SDK release flavor

## [2.4.2] - 2025-03-15
- Released empty version

## [2.4.1] - 2025-03-05
### Bug Fixes & Improvements
- Resolved Comment Creation bugs including:
  - Resolved various UI issues.
  - Fixed problems with the expanded state behavior.
  - Addressed cases where user mentions didn't work reliably.
  - Fixed scenarios where the Comment Creation text field cache failed.
  - Restored proper visibility of Comment Labels.
  - Added missing Reply/Edit title in Comment Thread.
  - Fixed visibility issues in the Error state.
- Restored Blitz layout disappearing when selecting the Typing + Comments state.
- Fixed Blitz not showing new comments when opened from Pre-Conversation.
- Corrected multiple UI state issues in Pre-Conversation.
- Prevented Conversation from disappearing after opening and closing the login screen.
- Fixed incorrect Notifications screen state after user logout.
- Removed unnecessary “View X Replies” indicator when replies are not present.
- Fixed issue with Social Reviews color customization.
- Corrected Pre-Conversation layout being cut off and missing the “Show more” button.
- Prevented Star Ratings from appearing when replying.
- Restored missing root comment content when replying in Conversation Regular/Light styles.
- Added ability to remove the online-user counter in Conversation.
- Improved timeout handling to prevent potential infinite loading loops.

## [2.4.0] - 2025-12-21
### New Features
- Introducing new feature - Nudges
- Introducing new design for CommentCreation ‘Floating’ type
- Add new api for customizing user name and comment count views
### Bug Fixes
- Sync the 'what do you think?' button state according to server
- Fix ‘Share’ button functionality
- Fix missing divider line after comment when it gets from blitz button
### Improvements
- Change cache system to use Room database
- Optimize data cache and synchronization with the server
- Optimize memory management and loading performance of comments, and improve scrolling exprience
- Optimize SDK initialization time
- Improve comments loading animation
- Stop collecting additional data using Sentry to avoid ANR crashes

## [2.3.3] - 2025-11-16
### Bug Fixes
- Scroll to posted comment doesn't work
- Tapping on New comment indicator doesn't show comment's GIF
- Collapse/Expand replies collapses long comment
- Regression - Comment's menu style is changed on PreConversation and Comment Thread
- Regression - Blitz indicator reappear after swiping it out
- Crash when loading disabled SpotId
- The app crashes when opening Notifications from Pre Conversation with enabled Filter Tabs
### Improvements
- Add Analytics events for all the SDK's API
- Support dynamic sort options from config
- Deprecate Ad events in OWViewActionCallbackType
### Customer-Specific Fixes
- Community guidelines blank when clicked from the awaiting review [CBS Sports]

## [2.3.2] - 2025-10-23
### Bug Fixes
- Fixed rare crash caused by duplicate view-holder IDs
### Improvements
- Upgraded Sentry SDK to version 8.23.0
- Reduced Sentry transaction and event volume

## [2.3.1] - 2025-10-05
### Bug Fixes
- Revert SDK api changes for version 2.3.0

## [2.3.0] - 2025-10-05
### Bug Fixes
- Fixed refresh not working in Full Conversation Fragment.
- Resolved incorrect GIF preview sizing.
- Fixed crash when changing orientation in the Report Reasons screen.
- Ensured comment thread scrolls correctly to show the selected comment.
- Prevented multiple modal windows from opening on double-tapping "Mute."
- Disallowed editing or deleting rejected comments.
- Maintained display name color for mentions after orientation change.
- Resolved crash on error screen during refresh.
- Preserved entered comment when app is minimized and reopened.
- Enabled conversation refresh after login via Like or Post Comment.
- Preserved floating comment input on orientation change.
### Improvements
- Added shimmering effect when loading conversation.
- Hide 'Report' option when guest reporting is disabled.
- Enabled typing/create endpoint when user starts typing.
### Customer-Specific Fixes
- Fixed missing green active icon on comments.
- Upgraded Sentry library to version 8.20.0.

## [2.2.3] - 2025-09-11
### Improvements
- Add support for displaying featured comments.
- Enhance user avatar rendering to display user initials when applicable.
- Introduce a toggle option for pull-to-refresh functionality on conversation screens.
### Bug Fixes
- Fix visual issues with reply lines.
- Fix an issue where the content of a posted comment does not display when editing.
- Resolve a crash related to the use of OWConversationStyle.Compact.
- Correct customization behavior for floating comment creation.
- Fix an issue preventing the comment input from closing when using OWCommentCreationStyle.Light.
- Fix an empty state issue that affects the visibility of community guidelines.
- Resolve input handling issues on tablet devices.
- Improve theming and layout handling across input components and empty states.

## [2.2.2] - 2025-08-14
### Bug Fixes
- Fixed issue where user mentions were reset upon screen rotation.
- Resolved blank screen when viewing images in full screen.
- Improved support for RTL layouts in notification animations.
- Addressed multiple issues in Floating mode:
  - Original Smart Replies (SR) missing during comment editing.
  - Rate options not appearing after transitioning from reply to root comment.
  - Rating UI blinking in various scenarios (empty or pre-conversation states).
  - Collapse animation and scroll behavior improved for SR components.
  - SR count now updates correctly for comments pending approval.
  - Enhanced error handling for network exceptions.
### UI Improvements
- Fixed various UI bugs in Pre-Conversation and SR summary views.
- Made SR summaries expandable and scrollable.
- Maintenance & Enhancements
- Removed redundant observers and default values for comment sort options.
- Added option to configure initial sort in the sample app.
- Hid notifications features when login screen is not implemented.
### Customer-Specific Fixes
- Resolved comment and UI issues specific to Footballco and CBS Sports integrations.
- Fixed crash and UI bugs in CBS Sports app.
### QA Support
- Improved visibility of list items in MaterialAutoCompleteTextView during automated testing.

## [2.2.1] - 2025-07-31
### Bug Fixes & Improvements
- Fix error handling for crash prevention.
- Fix a potential view leak in the Comment Creation screen.
- Add support for 16KB memory page sizes (required on some Android 15 devices).

## [2.2.0] - 2025-07-16
### New Feature
- Introduced Social Review functionality.
### Bug Fixes & Improvements
- Improved landscape layout and visual consistency across conversation views, including notification bell, info section, and toast notifications.
- Enhanced Notification Bell functionality, including payload accuracy, background color, and behavior when entering conversations.
- Fixed redirection issues when accessing conversations via prompt links or PreConversation views.
- UI and localization fixes for error screens, closed conversation messages, badge labels, and comment editing.
- Resolved issues with comment block interactions (e.g., GIFs) and missing elements in empty conversations.
- Ensured stable SDK behavior across multiple entry types (Compact, Summary, Button Only).
- Improved customization support (e.g., initial sort option, info layout, brand colors).
- Optimized SDK configuration by removing the need to include the JitPack repository.
- Prevented multiple taps on SDK buttons to avoid repeated actions.

## [2.1.2] - 2025-06-08
### Bug Fixes & Improvements
- Share all possible approachable libraries between all project modules
- Fix wrong initialization provider name
- Remove NEW_TASK flag from FullScreenImageActivity

## [2.1.1] - 2025-05-29
### Bug Fixes & Improvements
- Fixed an issue where the empty state was not visible when a conversation had no content.
- Resolved a bug where fragment-level theme settings were overriding the activity-level theme, ensuring consistent theming across the app.
- Optimized the app startup initialization process to prevent it from being overridden by other apps configurations.
- Improved scrolling mechanism to better support multiple scrolling hierarchies within the UI.
### Features
- Introduced a customization API for the Info layout, allowing greater flexibility in UI configuration.

## [2.1.0] - 2025-05-12
### Bug Fixes & Improvements
- Resolved issues with the typing indicator disappearing after interactions and corrected text display.
- Fixed problems with like/reply feed text, image/GIF labels, timestamp formatting, and incorrect reference messages in reply threads.
- Addressed issues causing unexpected scrolling behavior and stuck pagination in conversations.
- Fixed missing bottom borders, dark mode styling inconsistencies, and UI glitches like truncated texts, shadowed icons, and article preview errors on fresh app launch.
- Resolved a stability issue that could cause the app to crash in some situations.
- Updated the UI to hide the 'Mute' option for unauthenticated users.
### Features
- Introduced a floating notification bell and feed to help users stay updated on interactions.
- Added support for Android 16.
- Added support for full-screen images
- Article headers and Filter tabs now hide on scroll for a smoother user experience.

## [2.0.1] - 2025-04-08
### Bug Fixes
- Improved stability by resolving multiple crash scenarios.
- Fixed UI inconsistencies across Comment Creation, profile, and landscape modes.
- Addressed issues related to token refresh, logout, and login flows.
- Resolved problems with comment visibility, text persistence, and editing behavior.
- Corrected analytics event tracking and color customization bugs.
### Improvements
- Optimized refresh-token handling process.
- Fixed viewableTime event incorrectly tracking after Comment Creation is opened.
### Customer-Reported Bug Fixes
- Fixed user account deletion issue. [Reported by OneFootball]
- Resolved crashes in the Android SDK. [Reported by FootballCo]
- Fixed theme crash related to Theme.MaterialComponents. [Reported by Yahoo]

## [2.0.0] - 2025-02-19
### Features
- Improved UI support for various screen orientations.
- Optimized performance and background processes.
- Enhanced analytics tracking for better insights.
- Expanded customization options for UI elements.
- Ensured compatibility with Android 15.
### Bug Fixes
- Resolved issues causing unexpected crashes.
- Addressed UI inconsistencies and alignment problems.
- Improved handling of sorting and filtering options.
- Standardized font styles across the application.
- Fixed navigation issues related to tab behavior.
### Customer-Reported Bug Fixes
- Fixed custom font display inconsistencies.
- Ensured correct analytics event tracking.
- Addressed visibility issues in certain UI modes.
- Corrected translation inconsistencies for different languages.
### API & SDK Changes
- Introduced new API functionalities for better integration.
- Standardized naming conventions across SDK components.
- Removed and optimized outdated initialization methods.
- Simplified customization options for developers.

## [1.25.1] - 2024-10-15
### Features
- Kotlin version downgraded back to 1.9.10

## [1.25.0] - 2024-09-30
### Features
- The SDK now supports the following languages: Arabic, Dutch, French, German, Hungarian, Indonesian, Italian, Japanese, Portuguese, Portuguese (Brazil), Spanish, Thai, Turkish, and Vietnamese.
- Language and locale change support has been added.
### Bug Fixes
- Default sorting remains when switching between Newest and All tabs.
- The sorting option is unaffected by the Newest filter tab.
- After filing an appeal, the label visibility has been corrected.
- The Online users label and the Sort By text do not overlap.
- Reply dates are no longer cut off.

## [1.24.0] - 2024-09-03
### Features
- Introduced the Mentions feature, allowing users to tag others in comments.
- Enabled partners to set a minimum character length for reported comments.
- Added Dutch to the list of supported languages.
- Moved arrows to the right side of the conversation as per custom UI requests from the Admin Panel.
- Enabled customization of colors for comment action buttons.
- Kotlin version upgraded to 2.0.0
### Bug Fixes
- Resolved an issue where the screen would flicker due to scrolling when a comment with 'Read More' was pressed.
- Fixed an unresponsive "X" button on the comment control in Landscape mode.
- Added missing error handlers for when comments or replies fail to load.
- Implemented a design fix to truncate labels appropriately.
- Fixed an issue where long comments would collapse after interacting with "View more replies."
- Corrected the placeholder style to match the intended design.
- Adjusted the background behind the keyboard to display correctly in Dark mode.
- Ensured comments reflect changes when a user is muted in the PreConversation.
- Fixed a crash that occurred when clicking on the login prompt in mobile guest mode.
- Resolved issues with navigation from UI Views screens.
- Fixed parsing of FilterTabs response in the CommentThread screen.
- Ensured comments display correctly with line breaks.
- Ensured the correct formatting of user display names after posting new comments or replies.
- Fixed an issue with root comment storage after posting a reply from PreConversation.
- Resolved an issue where comment text was cut off when clicking on the "See more" button.
### Customer-Specific Bug Fixes
- Resolved an issue with multiple instances in the stack.
- Fixed overlap issues with the GIF button and the "Sign up to post" button, as well as privacy text overlapping with the OpenWeb logo in German and Dutch translations.
- Fixed unresponsive back buttons and resolved crash issues within the FootballCo integration.

## [1.23.1] - 2024-07-29
### Fixed
- Fix missing strings in Dutch language
- Fix unable to hide article header in conversation
- Fix bug in customized dark color setting

## [1.23.0] - 2024-07-15
### Features
- Add support to Dutch language

## [1.22.1] - 2024-07-09
- Mistakenly released version

## [1.22.0] - 2024-06-30
### Features
- Add Filter-Tabs new feature to filter comments inside a conversation
### Fixed
- Scrolling is now responsive in all conditions.
- Navigation issues in Conversation threads have been corrected.
- GIF sizes are consistent between editing and posting.
- GIF posting issues are resolved.
- Fixed display issues with "0 new comments" notifications.
- New comments are highlighted properly.
- Addressed empty comment thread screens when opening comments with invalid IDs.
- In compact mode, incorrect display messages no longer appear for closed Conversations.
- Comment Creation screen styles have been corrected in both light and dark modes.
- Pre-Conversation and Comment Thread screens are aligned and compression issues have been resolved.
- The position and visibility of the popup menus and buttons have been corrected.
- The cursor is properly positioned in the input field during text entry.
- The keyboard appears correctly during comment creation.
- Inactive button behavior and styling has been improved on various screens.
- Restrictions preventing guest users from muting or reporting are enforced.
- Login prompts and redirection for guest users have been corrected.
- Conversations containing comments with links load correctly.
- Conversation screen display and functionality issues no longer after restoring the app.
- All missing strings in supported languages have been translated.
- Crashes in specific UI Views or replies no longer occur.
- Community Guidelines text and visual elements are handled properly in all modes and styles.

## [1.21.0] - 2024-03-19
### Features
- When a user's comment has been reported, that user can request an appeal.
- When a user's comment has been reported, that user can click Learn more from the Conversation to be informed about the status of the reported comment.
- The user experience has been improved through multiple UI updates to alignment, behavior, and styles.
### Fixed
- The version restriction on the 'com.google.crypto.tink:tink-android' library has been removed.
- If a nested comment is reported, the This message was reported displays.
- Comments can include gifs.

## [1.20.1] - 2024-02-14
### Fixed
- Fix unwanted redirections from PreConversation screen

## [1.20.0] - 2024-02-04
### Feature
- Add ViewMoreReplies view to dynamically load more replies to a comment
- Add Floating Comment Creation screen to add a comment without leaving the conversation
### Change
- Look and feel of comments and replies
### Fixed
- Fix crash in ThreadCommentId API - updated androidx.lifecycle:lifecycle to 2.6.1
- Fix bug not supporting system dark mode
- Fix crash when clicking on a link 
- Stop throwing NoInternetConnectionIOException when no internet's available
- Crash fixes

## [1.19.2] - 2023-12-28
### Fixed
- Fix crash when pre conversation opens with layoutListener.

## [1.19.1] - 2023-07-25
### Fixed
- Moved pre conversation style to conversation options.

## [1.19.0] - 2023-07-25
### Feature
- Users can now specify a report reason when reporting a comment that appears in the moderation panel, helping moderators make better decisions.
- Compact mode view is supported for preConversation.
### Fixed
- Toast messages no longer overlap with the last comment.
- Clicking a toast message from the preConversation no longer causes a blank toast message to appear.
- Replying to a comment from the preConversation creates a comment reply.
- A blank page no longer displays when the thread comment ID is invalid.
### Change
- Text for the mute user confirmation dialog now reads: After muting this user, you will no longer see their activity.
To manage your muted users, go to your profile > Privacy settings.

## [1.18.2] - 2024-07-11
### Fixed
- Fix crash when new-line characters were given in authorization token

## [1.18.1] - 2024-07-03
### Fixed
- Fix crash when PreConversationFragment was leaked while reopening the screen

## [1.18.0] - 2023-06-07
### Feature
- Integration with Maven Central - SDK is now available from this Repository Artifact as well, see documentation for more info
- Comment ID deeplink support
### Fixed
- Toast message position bugs for orientation and dark mode
- Fix a possible crash when encoding and decoding some persistence data
- Fix rare crash which could cause in a third party we are using
- Fix initialization configuration which might affect some of our partners

## [1.17.3] - 2023-03-15
### Fixed
- An issue in which muted comments showed differently between a cold start and after a refresh
- Hiding "threads" in which all comments are either muted / reported / deleted

## [1.17.2] - 2023-02-08
### Features
- Added analytic callback when muting a user (.COMMENT_MUTE_CLICKED)
### Fixed
- Showing mute option only when the user logged in
- Flickering in the Conversation when it's loaded
- Crash when opening an outside URL
- A potential crash when changing orientation
- An issue with duplicate text in comment creation
- Changing device orientation caused incorrect comment counter

## [1.17.1] - 2023-01-12
### Fixed
- Support disabling GIF option
- Start login flow should get the Activity Context

## [1.17.0] - 2023-01-12
### Features
- Added an option to mute user straight from the mobile application
- Added API to retrieve the full conversation as a fragment
- Comment and reply counter in the comment creation screen
### Fixed
- In some cases the SDK might change the locale format of the host application, we fixed that

## [1.16.0] - 2022-12-07
### Features
- Support mute feature
- Support hiding edit option according to backend configuration
- Encrypt sensitive data ourselves (by defualt) instead of EncryptedSharedPreferences
### Fix
- Better description for rejected comments
- Showing posted link in Admin Panel from conversation flow

## [1.15.5] - 2022-10-23
### Fixed
- Flickering/bouncing issues in conversation screen
- A bug in the "read more" option
- Fixed a potential crash which might happened in case the brand color in the configuration was not a valid one
- Saving user message in comment creation screen when the device screen rotating
- Align gifs properly in RTL languages
- Fixed hebrew translation for "show more comments"
### Changes
- We are using Open Sans font (same as before), however we added different font weights.
Those font weights required when providing a custom font. Please read the documentation for more info

## [1.15.4] - 2022-09-18
### Fixed
- Spinner was stuck after device orientation change

## [1.15.2] - 2022-08-29
### Added
- Support Android 13 (API level 33)
- Added a spinner while loading a conversation / changing sort option

## [1.15.1] - 2022-08-10
### Fixed
- Hotfix: Serialization error for comment status

## [1.15.0] - 2022-08-09
### Added
- An option to receive a callback when the user press on a user profile / avatar instead of opening the existing profile screen
- Showing the year in a date of a comment if it was not posted in the current year
### Fixed
- A crash when editing a comment
- Keeping the same sort mode when re-opening a conversation
- A possible crash when using the get conversation counters API

## [1.14.0] - 2022-07-21
### Added
- UI to see pending messages which require approval (visible only to the user who wrote those)
- Analytic event after a new comment created successfully (CREATE_MESSAGE_SUCCESSFULLY)
### Changes
- Removed a configuration for an activity export option which wasn't been used

## [1.13.2] - 2022-07-12
### Added
- Dates above comments are formatted in the SDK language
### Fixed
- Margin and UI issues between username and dates in RTL languages

## [1.13.1] - 2022-06-22
### Added
- Support hiding the share button according to remote configuration

## [1.13.0] - 2022-06-15
### Fixed
- Recovering from 403 error - retry failed requests
### Changes
- Move sensitive data to EncryptedSharedPreferences
### Internal
- Logger service

## [1.12.0] - 2022-06-06
### Added
- Added French support
- Support line break in comment text
### Fixed
- Recovering from 403 auth error and renewing SOO if needed
- Keep the conversation screen with the same sort option after posting a new comment
### Changes
- Improved `getUserLoginStatus` API, see documentation for the full details

## [1.11.0] - 2022-05-24
### Fixed
- Fixed SSO authentication flow
- Removing authentication token once expired and not valid anymore
### Changes
- Few changes in SSO related methods API, see our documentation for the full details
- Added renewSSOAuthentication function inside LoginDelegate
- Renaming function name inside LoginDelegate to startLoginUIFlow

## [1.10.2] - 2022-05-19
### Fixed
- Hotfix for a bug related to SSO authentication.
This is a temporary solution and a more robust solution will be done soon.

## [1.10.1] - 2022-05-18
### Added
- Add PostId to SpotImCustomUIDelegate callback
- Add application bundle id to webhook callback
### Fixed
- Decoding special characters back in comment counters API
- Issue in which a reply didn't appear under the original comment
- New comment sometimes appear with likes
- A potential race condition which may have caused to issues with authentication
### Internal
- Removed old services

## [1.10.0] - 2022-04-25
### Added
- API to set the initial sorting option when a conversation open up
### Fixed
- Decoding special characters back in comment counters API

## [1.9.8] - 2022-04-10
### Fixed
- Support postId with special characters

## [1.9.7] - 2022-04-05
### Fixed
- Fixed an issue with comment status which caused some comments to be hidden in the pre conversation

## [1.9.6] - 2022-03-31
### Added
- Like & Dislike customization
- Support disabling user avatar online indication
- Subscriber badge

## [1.9.5] - 2022-03-23
### Added
- Open create comment screen directly

### Fixed
- Read only message overlapping last comment in conversation screen

## [1.9.3] - 2022-03-15
### Added
- Custom read-only message

### Fixed
- Report comment open edit screen
- Share screen flickering when changing to landscape

## [1.9.2] - 2022-03-07
### Fixed
- Fix manifest conflicting provider (when installing multiple apps with the SDK)

## [1.9.1] - 2022-01-26
### Added
- Edit comment
- Real time viewing counter
- Support post button customization

### Fixed
- Adding image to comment in Android 12
- Realtime data service

## [1.9.0] - 2022-01-12
### Added
- Create comment with image

### Fixed
- Comment with image is not visible in pre-conversation
- Fix PendingIntent without FLAG_IMMUTABLE or FLAG_MUTABLE Crash on Android 12

## [1.8.0] - 2022-01-02
### Added
- Support Android 12 SDK
- New guest nickname design

### Fixed
- Deleted comment crash

## [1.7.2] - 2021-12-20
### Added
- Staff badges

### Fixed
- Comments counter displaying a negative value when comment is deleted

### Changed
- Upgraded Giphy dependency version to 2.1.12

## [1.7.1] - 2021-11-30
### Fixed
- Social login didn't work properly due to unhandled intent in LoginActivity

### Changed
- App language code according to mobile-sdk config
- Change empty conversion prompt

## [1.7.0] - 2021-11-17
### Added
- Support Android 11 (API level 30)
- Support GoogleAdsSDK V20 (breaking changes)

### Fixed
- Comment creation screen UI fixes

## [1.6.5] - 2021-11-11
### Fixed
- Crash after deleting comment in pre-conversation
- Real time view UI issue in pre-conversation

### Changed
- Show deleted comment feedback on pre-conversation

## [1.6.4] - 2021-11-04
### Fixed
- Upvote/Downvote remains highlight even if user cancels SSO
- Remove expired user token from authorization header when fetching user/data

## [1.6.3] - 2021-10-28
### Added
- SSO start login flow on pre-conversation interaction mode

### Fixed
- SSO start login flow on root activity mode - fix for logged in users
- Terms & privacy UI in pre-conversation with no comments

## [1.6.2] - 2021-10-21

### Added
- Add flag for RN - show login screen on root Activity

## [1.6.1] - 2021-09-23

### Fixed
- Commenting on empty conversation in read only should be disable  

## [1.6.0] - 2021-09-14

### Added
- Button only in pre-conversation
- Full conversation ad banner
- Read only conversation
- Get comments count

## [1.5.11] - 2021-08-19

### Fixed
- Analytics events fixes

## [1.5.10] - 05/08/2021
### Added
- Analytics event listener
- Add missing events

### Fixed
- No Comment placeholder layout fix
- Any transition from pre-conv to full should show the interstitial (if its enabled)

### Changed
- When the full conversation is opened by the publisher send "viewed" event instead of "main-viewed"


## [1.5.9] - 15/07/2021
### Fixed
- Reported comment reappears
- No comment - landscape support
- Reply text in dark mode is not clear

### Changed
- Move customBiData to conversation options


## [1.5.8] - 06/07/2021
### Added
- Option to set publisher custom BI data
- Landscape support (with flag)

### Fixed
- Custom UI for NAVIGATION_TITLE_TEXT_VIEW issue when useWhiteNavigationColor is enabled
- New creation screen title “Add a Comment” should be stylized as “Add a comment”

### Changed
- More detailed explanation for what happened on SDK errors - extend SpotException


## [1.5.7] - 23/06/2021
### Added
- Create comment screen new design (with flag)
- "Sort by" new design
- "Sort by" option text customization
- Navigation title customization
- Conversation footer customization
- Community guidelines text customization

### Changed
- Update divider UI in Conversation
- Remove avatar from comment creation

## [1.5.6] - 10/06/2021
### Added
- Community question
- Support community question TextView customization
### Fixed
- Signup To Post button state fix when not forced registered
### Changed
- Use one time token for websdk

## [1.5.5] - 03/06/2021
### Added
- Support sayCtrl TextView customization
- Add success callback to SDK initialization

### Fixed
- Community guidelines launch issue with night mode
- Add a comments page will dismiss any active PiP windows

### Changed
- Change location of login prompt above the the sticky header
- Signup To Post button (Comment Create) always enabled

## [1.5.4] - 25/05/2021
### Added
- Night mode support (Android 8 and above)

### Fixed
- Like status in pre-conversation is not updated when return from conversation
- Accessibility issues with Talkback

## [1.5.3] - 19/05/2021
### Fixed
- Dark mode web SDK support breaks external links in community guidelines
- Community guidelines without header UI issue

## [1.5.2] - 13/05/2021
### Fixed
- Revert use play-services-ads-lite to reduce APK size

## [1.5.1] - 12/05/2021
### Added
- Support dark mode in websdk

### Changed
- Improve community guidelines scroll behavior
- Block start/complete SSO if user is already logged-in

## [1.5.0] - 09/05/2021
### Added
- Login prompt

### Fixed
- Gif is missing for comment reply with text
- Setting null title and description is overridden by read request extract data
- SSO state only updated in on resume

### Changed
- Do not call user/data as part of SDK init logic
- Use play-services-ads-lite to reduce APK size
- Remove pubmatic code and SDK to reduce SDK size

## [1.4.0] - 29/04/2021
### Changed
- Clicking links in comments will open the URL custom chrome tab
- Clicking on my guest profile will open registration flow
- Remove Giphy dependency from the SDK

### Fixed
- Comment labels UI in comment creation screen
- Header scroll issue for empty comments list

## [1.3.16] - 20/04/2021
### Added
- Comment labels
- Internal: model tests

## [1.3.15] - 08/04/2021
### Fixed
- Terms and privacy links Links should open chrome tab
- Toggle Like/Dislike persistent

## [1.3.14] - 30/03/2021
### Added
- Profile page
- Community guidelines
- Hide article header flag in ConversationOptions

## [1.3.13] - 25/03/2021
### Added
- add Spotim.getRegisteredUserId and  SpotIm.getUserLoginStatusWithId API calls

## [1.3.12] - 24/03/2021
### Fixed
- change logic for embeding PreConversation in recycle view to replace fragment in all cases
- Change minimum DPI limit for displaying conversation header

## [1.3.11] - 15/03/2021
### Added
- Enable certificate to use Charles proxy on sample app
- Authentication playground Activity on sample app
- Full conversation for fox button on sample app

### Fixed
- Trigger registration instead of error message when the user is not log in on rank comment
- Remove the profile image from the Scrollview in comment screen
- Show only text with no gap if image for article header is missing or not loading

## [1.3.10] - 23/02/2021
### Added
- Support showing full conversation directly

### Fixed
- Fix opening Conversation Activity when there is already opened activity in the stack (deeplink to article support)

## [1.3.9] - 09/02/2021
### Added
- Support showing PreConversation fragment as a RecyclerView item using SPFragmentConstraintLayout helper class

## [1.3.8] - 18/01/2021
### Added
- Optemized ab_test fetching mechanism
- German translations
- Disable ads to subscribed users

### Removed
- Placeholders from AndroidManifest.xml to avoid compilation issues, we will need to re-add those once we have partners that wants social login

## [1.3.7] - 15/11/2020
### Fixed
- Gap above pre-conversation when there's no ad banner
- Crash on ResizableTextView

## [1.3.6] - 02/11/2020
### Added
- x-openweb-token header logic to all requests to support UM 2.0

### Fixed
- '1 new comment' indicator does not go back to show realtime typing counter after clicked
- Realtime typing counter counts current user as well
- Keyboard not closing when 'comment creation' screen closes on some cases
- Crash on React-Native because of recycler view items had animateLayoutChanges set to true

### Changed
- Android target SDK to 29
- Apply themed background to ads for better UX when on dark theme

## [1.3.5] - 21/10/2020
### Fixed
- Jumpy UI when user comes back from comments screen to pre-conversation do to webview ad load

## [1.3.4] - 12/10/2020
### Fixed
- Don't show 'show more comments' button if all comments are loaded

### Changed
- Fetch user data only when needed
- Load web video ad behind banner instead of invisible

## [1.3.3] - 08/09/2020
### Fixed
- Crash on conversation recycler view animation

## [1.3.2] - 23/08/2020
### Added
- Webview ads are now loaded in addition to our DFP banner ads

### Changed
- Hebrew translations

## [1.3.1] - 10/08/2020
### Fixed
- Crash fix on pre-conversation [#413](https://github.com/SpotIM/spotim-android-sdk/pull/413)

## [1.3.0] - 05/08/2020
### Added
- Added post gifs
- Pubmatic for better monetization
- Arabic, Spanish & Portuguese translations

### Removed
- Test 33 for old google ads, chosen to go with group

### Fixed
- Hebrew fonts missing spotim_core prefix
- Login screen social icons on the left side on Hebrew
- Crash when having more than one pre-conversation fragment

## [1.2.1] - 07/07/2020
### Fixed
- Webview ads container issue (too small and shows scrolling) - Webview is now ready to be enabled remotely
- Crash when showing a toast message on Android 25 and below

## [1.2.0] - 25/06/2020
### Added
- Added web view ads
- Social login
- Added possibility to open a few PreConversations at one time
- Added API to listen height of PreConversation

#### Fixed
- Crash on conversation adapter

## [1.1.2] - 27/05/2020
### Added
- OpenWeb brand
- Send monetization errors to Kibana

### Fixed
- Showing nickname field for registered users on comment creation screen
- Some users can't see their comment after posting unless refreshing
- interstitial only shown once per session

## [1.1.1] - 03/05/2020
### Fixed
- Crash related to DI
- Crash related to loading fragment sequence

## [1.1.0] - 12/04/2020

### Fixed
- Fixed issue when sort type doesn't change in sorting menu on main conversation after click on Blitz on PreConversation [#289](https://github.com/SpotIM/spotim-android-sdk/pull/289)
- "Show more" button brand color
- Fixed issue with not showing comments in some specific cases
- Change the colour from default to be according to the pub's config
- Crash after click on Blitz view on PreConversation
- The bottom of ‘Show more comments’ appear for a second
- User in guest mode can’t view his comment
- Comment stays in view after Deleting it
- Issue after posting 1st comment , user can’t see his comment, Blitz is thrown [#315](https://github.com/SpotIM/spotim-android-sdk/pull/315)
- Issue when new lines (\n) between sentences are ignored [#316](https://github.com/SpotIM/spotim-android-sdk/pull/316)

### Added
- Showing media content in comments

## [1.0.4] - 16/03/2020

### Fixed
- The main conversation comments sometimes disappears
- x-platform header is not set correctly
- x-app scheme send wrong

## [1.0.3] - 11/03/2020

### Changed
- Banner sizes
- Disable interstitial to Logged In members

### Fixed
- Profile empty state text
- Event type ‘viewed’ is sent each time the user scrolls to the Pre Conversation
- Ad to be on top of the conversation


## [1.0.2] - 05/03/2020

### Changed
- User Profile (Stage 2): Follow & UnFollow is working.
- Send the SDK version in the header.
- Logout API [#277](https://github.com/SpotIM/spotim-android-sdk/pull/277)

### Fixed
- Event ‘user-profile-unfollow-clicked’ returns in every click in profile page
- Sorting menu is in dark mode while the app is in light mode
- User Profile (UI Fixes & Changes)
- Sorting menu is in dark mode while the app is in light mode
- Bottoms of 'Leave the page?' message have wrong colour in Dark Mode
- Conversation count increment
- Dialog background colour on Android 10.

## [1.0.1] - 25/02/2020

### Changed

* Send the SDK version in the header

## [1.0.0] - 20/02/2020

### Added
- Pagination, you can see more posts while scrolling in profile screen.

### Fixed
- Bug while parsing the extract data
- Article extract is missing case
- Clear token before startSSO
- Fastlane deployment
- Loading demo articles issue
- User gets 'Leader' badge upon his first comment
- User's Avatar + username are not loaded upon his 1st comment

### Changed
- User data management [#221](https://github.com/SpotIM/spotim-android-sdk/pull/221)
- Remove duplication code
- Make library resources private
- Multidex removed
- The way our SDK notifies the parent app to show login UI from Intent to delegate
- Added retry requesting config in case request failed (no network, server error etc.) [#226](https://github.com/SpotIM/spotim-android-sdk/pull/226)
- Getting extract data in mobile (Call Async)
-

### Added
- Profile Feature (First Stage)
- User status API [#225](https://github.com/SpotIM/spotim-android-sdk/pull/225)
- ConversationOptions to configure current Conversataion [#239](https://github.com/SpotIM/spotim-android-sdk/pull/239)
- Added Article to the ConversationOptions.Builder()
- Added new API to get the pre-conversation with different sorting [#248](https://github.com/SpotIM/spotim-android-sdk/pull/248)
- Mundo Deportivo (Demo App)

## [0.1.9] - 01/01/2020

### Added
- Error reporting (Kibana)
- Feature flag (Blitz/Realtime)
- Add prefix to resources to prevent conflicts
- Support Logout It's available using SpotIm.logout()
- Support Proguard optimisation (consumer-proguard-rules.pro)

### Fixed
- Fix Events issues
- Fix Hebrew translation issues

### Removed
- Remove `language` param from SpotIm.gePreConversation() method

### Changed
- Update API functionality to be working in a synchronize way
- Using OpenSans font in the Hebrew language

<!-- Links -->
[Unreleased]: https://github.com/SpotIM/spotim-android-sdk/compare/1.1.2...dev
[0.1.9]: https://github.com/SpotIM/spotim-android-sdk/releases/tag/0.1.9
[1.0.0]: https://github.com/SpotIM/spotim-android-sdk/releases/tag/1.0.0
[1.0.1]: https://github.com/SpotIM/spotim-android-sdk/releases/tag/1.0.1
[1.0.2]: https://github.com/SpotIM/spotim-android-sdk/releases/tag/1.0.2
[1.0.3]: https://github.com/SpotIM/spotim-android-sdk/releases/tag/1.0.3
[1.0.4]: https://github.com/SpotIM/spotim-android-sdk/releases/tag/1.0.4
[1.1.0]: https://github.com/SpotIM/spotim-android-sdk/releases/tag/1.1.0
[1.1.1]: https://github.com/SpotIM/spotim-android-sdk/releases/tag/1.1.1
[1.1.2]: https://github.com/SpotIM/spotim-android-sdk/releases/tag/1.1.2
