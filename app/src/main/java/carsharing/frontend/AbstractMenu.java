package carsharing.frontend;

import carsharing.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alex Giazitzis
 */
public abstract class AbstractMenu implements Menu {
    protected final Map<String, Runnable> options;
    protected final List<String>          optionsText;
    protected       boolean               exit;

    protected AbstractMenu() {
        options = new HashMap<>();
        optionsText = new ArrayList<>();
        exit = false;
    }

    @Override
    public void show() {
        while (!exit) {
            optionsText.forEach(System.out::println);
            int choice = Integer.parseInt(Main.scanner.nextLine());
            System.out.println();
            execute(choice);
        }
    }

    protected void execute(final int choice) {
        String option = optionsText.stream().filter(s -> s.startsWith(String.valueOf(choice))).findFirst().orElse("");
        if (option.isEmpty()) {
            System.out.println("Invalid option chosen. Please choose one from the list shown.");
            return;
        }
        options.get(option).run();
    }

    abstract protected void setOptions();

    protected final void exit() {
        exit = true;
    }
}
