# Pokéllection Application Manual

Our Pokéllection application allows users to collect, organize, and manage Pokémon data. This application pulls data from the PokeAPI to populate the Pokémon collection and displays it through a Java Swing graphical user interface, providing features for searching, filtering, and persisting collections to files.

Here's a comprehensive manual to help users navigate our application.

---

## Table of Contents
1. [Introduction](#1-introduction)
2. [Getting Started](#2-getting-started)
3. [Application Overview](#3-application-overview)
4. [Viewing Pokémon Details](#4-viewing-pokémon-details)
5. [Searching and Filtering](#5-searching-and-filtering)
6. [Sorting Pokémon](#6-sorting-pokémon)
7. [Creating and Saving Collections](#7-creating-and-saving-collections)
8. [Troubleshooting](#8-troubleshooting)

---

## 1. Introduction

**Pokéllection** is an application that allows you to browse, search, and collect your favorite Pokémon. The application connects to the [PokeAPI](https://pokeapi.co/) to fetch data for the first 100 Pokémon, displaying their images, types, and detailed statistics. You can create custom collections of your favorite Pokémon and save them for future reference.

---

## 2. Getting Started

### System Requirements
- Java Runtime Environment (JRE) 11 or higher
- Internet connection (for initial data fetching)

### Installation
1. Download the Pokéllection application JAR file.
2. Double-click the JAR file to launch the application, or run it from the command line:

``java -jar Pokellection.jar``

---

## 3. Application Overview

When you first launch Pokéllection, the application will:

1. Connect to the PokeAPI to fetch data for the first 100 Pokémon.
2. Display the main interface divided into two panels:
- **Left panel**: List of Pokémon with search and filter options
- **Right panel**: Detailed view of the selected Pokémon

![The main application interface showing both panels.png](Screenshots/The%20main%20application%20interface%20showing%20both%20panels.png)
> _The main application interface shows both panels._

---

## 4. Viewing Pokémon Details

The right panel displays detailed information about the selected Pokémon:

1. Name and ID number
2. Pokémon image
3. Type information (displayed as colored tags)
4. Base stats with visual bar representation:
- HP
- Attack
- Defense
- Special Attack
- Special Defense
- Speed
5. Base stats total (sum of all stats)

To view a Pokémon's details, simply click on its name in the list on the left panel.

![Details panel for Pikachu.png](Screenshots/Details%20panel%20for%20Pikachu.png)
> _Details panel for Pikachu_

---

## 5. Searching and Filtering

### Searching for Pokémon
1. Use the search box at the top of the left panel
2. Type a Pokémon's name or ID number
3. The list will automatically update to show only Pokémon matching your search term
4. Clear the search box to show all Pokémon again

![Search result for "char" showing Charmander, Charmeleon, Charizard.png](Screenshots/Search%20result%20for%20%22char%22%20showing%20Charmander%2C%20Charmeleon%2C%20Charizard.png)
> _Search result for "char" showing Charmander, Charmeleon, Charizard._

### Filtering by Type
1. Use the **Type** dropdown menu in the left panel
2. Select a Pokémon type (Fire, Water, Electric, etc.)
3. The list will update to show only Pokémon of the selected type
4. Select **"All Types"** to remove the filter

![Showing only grass-type Pokemon.png](Screenshots/Showing%20only%20grass-type%20Pokemon.png)
> _Showing only grass-type Pokémon._

---

## 6. Sorting Pokémon

You can sort the Pokémon list in different ways:

1. Click on the **"Sort by"** dropdown menu
2. Choose a sorting option:
- Name A-Z
- Name Z-A
- ID ↑ (ascending)
- ID ↓ (descending)
- HP ↓ (descending by HP stat)

The list will immediately update to reflect your sorting preference.

![Sort dropdown menu expanded.png](Screenshots/Sort%20dropdown%20menu%20expanded.png)
> _Sort dropdown menu expanded._

---

## 7. Creating and Saving Collections

You can create custom collections of your favorite Pokémon:

1. Browse through the Pokémon list
2. Click the **checkbox** next to each Pokémon you want to add to your collection
- The checkbox appears to the left of each Pokémon in the list
- You can select multiple Pokémon
3. Click the **"Save Team"** button at the bottom of the left panel
4. The application will automatically save your collection as a **JSON** file with an auto-incremented name (`team1.json`, `team2.json`, etc.)
5. A confirmation message will appear showing the filename of your saved collection

![Several Pokemon with checkboxes selected.png](Screenshots/Several%20Pokemon%20with%20checkboxes%20selected.png)
> _Several Pokémon with checkboxes selected_  

![The save confirmation dialog.png](Screenshots/The%20save%20confirmation%20dialog.png)
> _The save confirmation dialog._

---

## 8. Troubleshooting

### Common Issues

#### No Pokémon display when launching the application
- Check your internet connection
- The application requires internet access to fetch Pokémon data from the PokeAPI
- If the problem persists, try restarting the application

#### Images not loading
- Ensure you have internet access (images are loaded from the PokeAPI)
- Some images might take longer to load depending on your connection speed
- If an image fails to load, you'll see **"Error loading image"** in place of the Pokémon sprite

#### Search or filter not working
- Ensure you've spelled the Pokémon name correctly
- Try using partial names (e.g., `"char"` instead of `"charizard"`)
- If filtering by type doesn't work, try selecting **"All Types"** and then your desired type again

#### Unable to save collection
- Ensure your application has write permissions for the directory it's running in
- Try selecting at least one Pokémon before attempting to save

---

## Need more help?

For additional assistance, please contact the development team:  
📧 **ren.jiani@northeastern.edu**

