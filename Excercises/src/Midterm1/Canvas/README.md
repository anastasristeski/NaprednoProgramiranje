# ShapesApplication

The `ShapesApplication` class is designed to manage and process data for multiple canvases that display geometric shapes in the form of squares.

## Class Definition

### Constructor
- **`ShapesApplication()`**  
  Initializes an instance of the `ShapesApplication` class.

### Methods
#### 1. `int readCanvases(InputStream inputStream)`
Reads data about multiple canvases from an input stream. Each line of the input stream represents information for one canvas in the following format:
canvas_id squares_count total_squares_perimeter

- `canvas_id`: The ID of the canvas with the largest total perimeter.
- `squares_count`: The number of squares in the canvas.
- `total_squares_perimeter`: The total perimeter of all squares in the canvas.

## Example Usage
The class provides functionality for reading canvas data and determining which canvas has the largest total square perimeter, ensuring efficient processing and output formatting.
