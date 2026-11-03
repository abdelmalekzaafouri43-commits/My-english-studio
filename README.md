# My English Studio 🦉

A comprehensive personal application for English teachers to manage students, worksheets, lesson plans with AI, teaching scripts, cartoon characters, and video projects.

## 🚀 Live Demo & GitHub Pages

This repository includes a root `index.html` file configured for instant deployment on **GitHub Pages**, Vercel, or Netlify:

- **GitHub Pages Root**: `/index.html`
- **Mobile & Desktop Responsive**: Supports all screen sizes with a left dashboard sidebar.

## 📱 Features

1. **Worksheets Library**: Upload PDF worksheets & photos, tag by lesson title, topic, and school level.
2. **My Students Roster**: Track student profiles, English CEFR levels, attendance, and assigned worksheets.
3. **AI Lesson Plan Generator**: Automatically generate 5-phase structured lesson plans with target focus and learning objectives.
4. **Teaching Scripts**: Prepare dialogues with interactive text-to-speech auto-reader & speed controls.
5. **Cartoon Characters**: Organize teacher & student avatars with signature voice catchphrases.
6. **Video Projects**: Manage video scripts, voiceover tracks, subtitles, and MP4 exports.

## 🤖 Android Application

The project is structured as a native Android project using Gradle & Jetpack Compose:

- Android assets: `app/src/main/assets/index.html`
- Android WebView Container: `app/src/main/java/com/example/ui/MainStudioApp.kt`

To build the Android APK:
```bash
./gradlew assembleDebug
```
The APK will be generated at `app/build/outputs/apk/debug/app-debug.apk`.
