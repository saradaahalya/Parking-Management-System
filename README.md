# Advanced Parking Management System 🚗

A sophisticated Java-based parking management system with both GUI and CLI interfaces.

## Features ✨

- **Dual Interface**
  - Modern Swing-based GUI with intuitive controls
  - Command-line interface for terminal operations

- **Smart Parking Management**
  - VIP slot allocation (20% of total slots)
  - Dynamic rate management (Regular/VIP/Weekend)
  - Automatic nearest slot assignment
  - Real-time revenue tracking
  - Comprehensive statistics

- **Vehicle Management**
  - Detailed car registration (number plate, owner, type, color)
  - VIP vehicle support
  - Smart search functionality
  - Duration-based billing

- **Rate Management**
  - Configurable rates for different categories
  - Weekend surcharge support
  - VIP premium rates
  - Real-time rate updates

## Getting Started 🚀

### Prerequisites
- Java JDK 11 or higher
- Any Java IDE (preferably VS Code with Java extensions)

### Running the Application

1. **Compile the source files:**
```bash
javac src/*.java
```

2. **Run the application:**
```bash
java src.Main
```

3. **Choose your preferred interface:**
   - Enter `1` for GUI mode
   - Enter `2` for CLI mode

## Usage Guide 📖

### GUI Mode
- Use the top panel to monitor statistics and update rates
- Central grid shows parking slot status
- Bottom panel provides action buttons:
  - 🅿️ Park Car
  - 🚫 Remove Car
  - 🔍 Search Car
  - 📊 Show Statistics
  - 🔄 Refresh

### CLI Mode
1. Enter total parking slots when prompted
2. Use the menu options:
   - Park a Car
   - Remove a Car
   - Display Status
   - Search Car
   - Show Statistics
   - Update Rates
   - Exit

## Rate Structure 💰

- **Regular Rate**: $10/hour
- **VIP Rate**: $20/hour
- **Weekend Rate**: $15/hour
- Minimum billing: 1 hour
- Rates can be updated through GUI or CLI interface

## Technical Details 🔧

- Built using Java Swing for GUI
- Implements priority queue for optimal slot allocation
- Uses HashMap for O(1) car lookup
- Real-time statistics calculation
- Event-driven architecture
- Modern UI with material design colors

## Future Enhancements 🔮

- [ ] Database integration
- [ ] Payment gateway integration
- [ ] Monthly pass system
- [ ] Mobile app integration
- [ ] Automated license plate recognition
- [ ] Email notifications

## Contributing 🤝

Feel free to fork this project and submit pull requests. For major changes, please open an issue first.

## License 📄

This project is licensed under the MIT License - see the LICENSE file for details.