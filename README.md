# React Native Android Picture in Picture

Add picture in picture support to react native android application.
Also has a listener to notify the pip state change.

## Note

This package only works on android.

## Demo

<a href="https://github.com/victorleduc/react-native-pip-android"><img src="https://user-images.githubusercontent.com/26771716/130575748-d763dc3c-ff73-4727-8019-28eb210c88fd.gif" width="360"></a>

## Installation

Using npm

```sh
npm install react-native-pip-android-no-native-needed
```

or using yarn

```sh
yarn add react-native-pip-android-no-native-needed
```

## Setup

Add the following attrs in `/android/app/src/main/AndroidManifest.xml` file

```xml
  <activity
    ...
      android:supportsPictureInPicture="true"
      android:configChanges=
        "screenSize|smallestScreenSize|screenLayout|orientation"
        ...
```

## Usage

```js
import { NativeModules } from 'react-native';
import PipHandler, { usePipModeListener } from 'react-native-pip-android';

export default function App() {
  const PipAndroidModule = NativeModules.PipAndroid;

  useEffect(() => {
    // Register for Android Lifecycle Event Observer
    PipAndroidModule?.registerLifecycleEventObserver();
  }, []);

  // Use this boolean to show / hide ui when pip mode changes
  const inPipMode = usePipModeListener();

  if (inPipMode) {
    return (
      <View style={styles.container}>
        <Text>PIP Mode</Text>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <Text style={styles.text}>
        These text components will be hidden in pip mode
      </Text>
      <TouchableOpacity
        onPress={() => PipHandler.enterPipMode(300, 214)}
        style={styles.box}
      >
        <Text>Click to Enter Pip Mode</Text>
      </TouchableOpacity>
    </View>
  );
}
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
