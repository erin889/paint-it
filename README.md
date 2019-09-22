> A vector-drawing program, consisting of a tool palette and a canvas.


#### Feature Implemented

Menu bar with a File menu:
1. New: Clear existing canvas and create a new drawing
2. Load: Load from an existing file with supported format (.myday) to canvas
3. Save: Save the current drawing as (.myday) file

A tool palette:
1. Select: select the shape which is on the top of current mouse coordinates. Pressing ESC to deselect. Drag the shape to move around.
2. Eraser: click on a shape to erase it
3. Line: draw a line with selected color and line thickness
4. Circle: draw a circle with black border and selected shape color
5. Rectangle: draw a rectangle with black border and selected shape color
6. Fill: click on a shape to fill with current selected color

A color palette and line thickness palette:

Select a shape and those palettes will update selected color and thickness accordingly.

Additional Features:

Support system-level copy/paste: use ctrl+c/v to copy and paste the canvas as an image to an outside program/

Window Resizing: The tool palettes will expand and adapt upon available space if the window size is increasing. The canvas will display scrollbars if window size is smaller. A minimum size is set to the application window.


## Setup

```sh
gradle run
```
Note: This application is developed through macOS High Sierra.

## Reference

Icons obtained from: https://www.flaticon.com/authors/freepik
