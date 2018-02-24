添加依赖：
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
 Add the dependency

	dependencies {
	        compile 'com.github.c786909486:ViewPagerBanner:v1.0'
	}