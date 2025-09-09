# Pokémon Collection Management System (Pokéllection)

Personal extended version of my CS5004 Final Project.  
Originally developed as a team project, this repository highlights **my individual contributions and extended improvements**.

---

## 🎮 About the App
**Pokéllection** is a desktop application built with **Java Swing** following the **MVC (Model–View–Controller)** architecture.  
The app integrates with the [PokeAPI](https://pokeapi.co/) to fetch detailed information for the first 100 Pokémon, including their images, types, and stats.

With this app, users can:
- Explore a catalog of the first 100 Pokémon  
- View detailed statistics and type information  
- Search and filter Pokémon by name and type  
- Create and manage custom Pokémon teams  
- Save and load teams for future reference  

---

## ✨ Features
- **Search & Filter**: Quickly find Pokémon by name or type  
- **Team Management**: Add, remove, save, and load custom Pokémon teams  
- **Interactive GUI**: Intuitive list panel and detail panel built with Java Swing  
- **Custom UI**: Checkboxes, combo boxes, and custom renderers for a better user experience  
- **Persistence**: Save teams locally and reload them across sessions  
- **Testing**: JUnit tests for models, controllers, and view components  

---

## 🛠️ Tech Stack
- **Language**: Java  
- **Framework**: Swing (GUI)  
- **Architecture**: MVC (Model–View–Controller)  
- **Build Tool**: Gradle  
- **Testing**: JUnit  

---

## 🚀 Getting Started

### Prerequisites
- Java 17+  
- Gradle  

### Build & Run
```bash
# Clone the repository
git clone https://github.com/lanselylee/pokemon-collection-app.git
cd pokemon-collection-app

# Build the project
gradle build

# Run the application
gradle run
