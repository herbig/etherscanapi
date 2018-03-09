# Etherscan.io API wrapper for Android

A unofficial wrapper library for the [Etherscan.io](https://etherscan.io/apis) API for Android written in Kotlin.

### Installing

Add the JitPack repository to your project's build.gradle file:

```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

Add the dependency to your app's build.gradle file:

```
dependencies {
  compile 'com.github.herbig:etherscanapi:1.0.1'
}
```

### Usage

Instantiate with your Etherscan.io API key and make a request:

```
var api = Etherscan("YOUR_API_KEY")

api.getContractABI("0xBB9bc244D798123fDe783fCc1C72d3Bb8C189413")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                { result ->
                    hello.text = result.result
                },
                { e ->
                    Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
                }
        )
```
