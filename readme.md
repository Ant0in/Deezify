<div align="center">
  <h1>Deezify</h1>
  <p>🎸A fully-fledged music player.</p>
<p><i>INFO-F307 at <a href="https://ulb.be">ULB</a></i></p>
</div>

## Table of Contents
- [📖 Overview](#-overview)
- [🔧 Installation](#-installation)
   - [Binaries](#binaries)
   - [Compile from source](#compile-from-source)
- [🗺️ Roadmap](#-roadmap)
   - [Phase 1](#-phase-1)
   - [Phase 2](#-phase-2)
   - [Phase 3](#-phase-3)
   - [Phase 4](#-phase-4)
- [🤝 Acknowledgements](#-acknowledgements)

## 📖 Overview
Deezify is a music player application developed as part of the INFO-F307 course at ULB. The project aims to provide a simple, intuitive, and feature-rich music player for desktop platforms.
It is developed using Java and JavaFX, and it supports playback of local audio files (`.mp3`, `.wav`).

It is available for Windows, macOS, and Linux.


## 🔧 Installation
### Binaries

To run the project, you can download the latest release from the [releases page](https://gitlab.ulb.be/ulb-infof307/2025-groupe-2/-/releases).
You'll need to have [Java](https://www.java.com/en/download/) installed on your system.

### Compile from source

1. Clone the repository

```bash
git clone https://gitlab.ulb.be/ulb-infof307/2025-groupe-2.git
```

2. Install the Java JDK compatible with your platform:
   https://www.oracle.com/java/technologies/downloads/

3. Install the JavaFX SDK compatible with your platform:
   https://gluonhq.com/products/javafx/

4. Install Maven on your system:
   https://maven.apache.org/download.cgi

5. Compile the project

```bash
mvn compile
```

6. Run the project

```bash
mvn exec:java
```

## 🗺️ Roadmap
### 📌 Phase 1
- [x] 🎵 **Basic Music Playback**: Plays local audio files (`.mp3`, `.wav`)
- [x] 🎚️ **Playback Controls**: Play, pause, skip, previous track, seek, volume control
- [x] 📌 **Queue System**: Adds/removes tracks to the playback queue


### 📌 Phase 2
- [x] 🌎 **Multilingual Support**: Supports English, French, and Dutch
- [x] 🖼️ **Album Art Retrieval**: Fetch & display album covers automatically
- [x] 📜 **Metadata Editing**: Modify artist, track name, and album information
- [x] 🏷️ **Tagging System**: Assign custom tags (genres, moods, events) to tracks
- [x] 📑 **Playlists Management**: Create, edit, and organize playlists
- [x] ⭐ **Favorites System**: Easily mark and access favorite songs
- [x] 📻 **Web Radio Integration**: Stream online radio stations
- [x] 🎤 **Lyrics Display**: Display lyrics for the currently playing track
- [x] 🎧 **Equalizer**

### 📌 Phase 3
- [x] 🔎 **Research**: Lets the userProfile search for music in its library based on title, artist, album, or tags 
- [x] 〰️ **Visualizer**: Audio Visualizer, draws a nice looking graph that updates based on the sounds that are playing
- [x] 📜 **Metadata autocompletion**: Proposes values for artist, album, and tags when modifying the metadata
- [x] 🎤 **Karaoke**: Allows for the userProfile to sing along with the music playing thanks to lyrics updating in real time with the song
- [x] 🎛️ **DJ mode**: Allows for audio effects to be applied in real time

### 📌 Phase 4
- [x] 🤖 **Smart Suggestions**: Suggests similar tracks during playlist creation based on artist, tags, and album metadata.
- [x] 🔄 **Crossfade Transitions**: Smooth transitions between tracks with adjustable fade duration.
- [x] 🎬 **Video Cover Support**: Plays a muted `.mp4` video as the song’s cover if set.
- [x] 👥 **User Accounts (Local)**: Multiple local users with separate libraries and playlists, plus a shared global folder.

## 🤝 Acknowledgements
This project uses the following libraries:
- [JavaFX](https://openjfx.io/)
- [JAudiotagger](https://bitbucket.org/ijabz/jaudiotagger/src/master/)
- [GSon](https://github.com/google/gson)
- [JUnit](https://junit.org/junit5/) for testing