# inx-textfield

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-inx--textfield-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/8126)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com/)

**inx-textfield** is a premium, lightweight Android library that provides highly customizable and animated text input fields. Built with a focus on modern aesthetics and smooth user experience, it allows developers to create beautiful forms with minimal code.

## ✨ Features

- 🎨 **Fully Customizable**: Control primary, secondary, error, and background colors.
- 🎬 **Smooth Animations**: High-performance floating label transitions.
- 🛠 **Real-time Validation**: Built-in support for error states and helper texts.
- 📱 **Material 3 Ready**: Designed to look great with modern Material Design standards.
- 🌗 **Dark Mode Support**: Seamlessly transitions between light and dark themes.
- 🌍 **RTL Support**: Native support for Right-to-Left languages.

## 🚀 Installation

Add the following to your root `build.gradle` file:

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency to your app's `build.gradle`:

```gradle
dependencies {
    implementation 'com.github.InheritxSolution:inx-textfield:1.0.0'
}
```

## 📖 Usage

### Basic Example

```xml
<com.inheritx.standardedittext.CustomFrameLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:labelText="User Name"
    app:primaryColor="@color/st_primary">

    <com.inheritx.standardedittext.StandardEditText
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Enter your name" />

</com.inheritx.standardedittext.CustomFrameLayout>
```

### Custom Attributes

| Attribute | Description |
| --- | --- |
| `app:labelText` | The floating label text. |
| `app:helperText` | The text shown below the input field. |
| `app:primaryColor` | Color used when the field is focused. |
| `app:errorColor` | Color used when the field has an error. |
| `app:useSpacing` | Enables a denser layout for compact UIs. |

## 🏗 Architecture

The library is built using a clean, modular approach:
- **Library Module**: Decoupled UI logic for maximum reusability.
- **Showcase App**: Built with **Kotlin** and **MVVM** to demonstrate best practices in modern Android development.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

---
Developed with ❤️ by **Inheritx Solutions**
