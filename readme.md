<div style="text-align: center">
  <h1>Deezify</h1>
  <p>🎸A fully-fledged music player.</p>
<p><i>INFOF-307 at <a href="http://ulb.be">ULB</a></i></p>
</div>

## Table of Contents
- [📖 Overview](#-overview)
- [🔧 Installation](#-installation)
   - [Binaries](#binaries)
   - [Compile from source](#compile-from-source)
- [🗺️ Roadmap](#roadmap)
   - [Phase 1](#-phase-1)
   - [Phase 2](#-phase-2)
- [🤝 Aknowledgements](#aknowledgements)

## 📖 Overview
Deezify is a music player application developed as part of the INFOF-307 course at ULB. The project aims to provide a simple, intuitive, and feature-rich music player for desktop platforms.
It is developed using Java and JavaFX, and it supports playback of local audio files (`.mp3`, `.wav`).

It is available for Windows, MacOS, and Linux.


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

2. Install the JavaFX SDK compatible with your platform:
   https://gluonhq.com/products/javafx/

3. Install Maven on your system:
   https://maven.apache.org/download.cgi

4. Compile the project

```bash
mvn compile
```

5. Run the project

```bash
mvn exec:java
```

## 🗺️ Roadmap
### 📌 Phase 1
- [x] 🎵 **Basic Music Playback**: Play local audio files (`.mp3`, `.wav`)
- [x] 🎚️ **Playback Controls**: Play, pause, skip, previous track, seek, volume control
- [x] 📌 **Queue System**: Add/remove tracks to the playback queue


### 📌 Phase 2
- [x] 🌎 **Multilingual Support**: Support English, French, and Dutch
- [x] 🔍 **Search Functionality**: Find songs based on title, artist, album, or tags
- [x] 🖼️ **Album Art Retrieval**: Fetch & display album covers automatically
- [ ] 📜 **Metadata Editing**: Modify artist, track name, and album information
- [ ] 🏷️ **Tagging System**: Assign custom tags (genres, moods, events) to tracks
- [ ] 📑 **Playlists Management**: Create, edit, and organize playlists
- [ ] ⭐ **Favorites System**: Easily mark and access favorite songs
- [ ] 📻 **Web Radio Integration**: Stream online radio stations
- [ ] 🎤 **Lyrics Display**: Display lyrics for the currently playing track
- [ ] 🎧 **Equalizer**

## 🤝 Aknowledgements
This project uses the following libraries:
- [JavaFX](https://openjfx.io/)
- [JAudiotagger](https://bitbucket.org/ijabz/jaudiotagger/src/master/)
- [JUnit](https://junit.org/junit5/) for testing