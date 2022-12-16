<h1><img src="https://github.com/jakegallon/image-editor/blob/master/src/res/icon.png" width="64" height="64" alt="logo"/> ImageEditor</h1>
<p>ImageEditor is an image-editing software written in Java. It is primarily designed to be used to create sprite sheets, with some features purposefully built to assist their creation, however this software can also be used as a general image editor.</p>
<p></p>
<h2>Features</h2>
<p>In this software, a canvas refers to the image created by the user. A canvas is made up of multiple layers, stores its own undo/redo data, grid information, and more. A canvas can be exported as an image.</p>
<ul>
  <li><b>Tools</b>: Pen, Erase, Select, Move, Color picker and Fill tools, with editable properties (such as "width", "opacity" and "anti aliasing") and subtools (such as "move layer" and "move selection"), selectable through key bindings
  <li><b>Layers</b>: Seperate layers which make up a canvas and can be created, rearranged, merged, locked, hidden, and deleted.
  <li><b>Undo/Redo system</b>: Full support for undo and redo for all operations, using CTRL+Z and CTRL+Y.
  <li><b>Clipboard support</b>: Support for cut, copy and paste to and from the computer clipboard, either for the whole canvas or limited to a selection created by the user.
  <li><b>Canvas Saving/Loading</b>: Custom file format (.jmg) with functionality for "save", "save as", and "open", to restore all related information including layers and grid information.
  <li><b>Canvas Exporting</b>: The canvas can be exported as a .png, .jpg, .bmp, or .tiff image, retaining data such as transparency where applicable.
  <li><b>Grid cells and ghosting</b>: sprite sheet oriented functionality for displaying cells over the canvas, optionally displaying a ghost of each cell in the next cell, and optionally wrapping the ghost horizontally around the canvas in the last cell.
  <li><b>Animation</b>: sprite sheet oriented functionality for displaying multiple grid cells of the canvas in chosen order at an alterable rate, made to preview what a created sprite animation will look like, with multiple preset animation panel layouts.
  <li><b>Color picking system</b>: A square colour circle system is included where any colour can be selected visually, or entered by either RGB, HSV or Hex. Colours can also be saved to and restored from a palette.
  <li><b>Magnification panel</b>: Mini-map like panel with two modes and configurable zoom, automatic (when zoomed in, it displays an overview of the whole canvas. when zoomed out, it zooms in around the mouse), and manual (always zoomed around mouse).
  <li><b>Tabs</b>: Allows multiple canvas' to be opened at the same time.
</ul>
<h2>Credits</h2>
<p>One third-party library is used in this project:</p>
<ul>
  <li><a href="https://github.com/JFormDesigner/FlatLaf">FlatLAF</a>
</ul>
