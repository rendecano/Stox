# Stox
An app to demonstrate MVVM + Android Architecture components with dark UI theme.

# Screenshot
![Alt text](/screenshots/stock_list.png?raw=true "Screenshot")
![Alt text](/screenshots/favorites.png?raw=true "Screenshot")
![Alt text](/screenshots/stock_details.png?raw=true "Screenshot")
![Alt text](/screenshots/settings.png?raw=true "Screenshot")
![Alt text](/screenshots/settings_dark.png?raw=true "Screenshot")

# Architecture 
MVVM 

# Language
Kotlin

# Tech/Tools/Libs Used
- Kotlin Coroutines
- DataBinding
- Room
- ViewModel
- LiveData
- Glide
- Retrofit
- OkHttp
- Gson
- Lottie
- Material Components 
- MPChart

# API Used
Financial Modeling
- https://financialmodelingprep.com/api/v3/company/stock/list

# App Icon
 
# Play Store
<a href='https://play.google.com/store/apps/details?id=com.teamdecano.cryptocoin&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png'/></a>
 
# Android versions
- Android 5.1 above

# Further Improvements
- Cache timeout validation

# Challenges encountered
- API not optimised, pagination could have helped
- Other info could have been returned on the initial API to make it lightweight
- May introduce DOS attack, calling API more than 7000 times won't help the server

