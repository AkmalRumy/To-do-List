
package pdsa.cw;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;
import java.util.InputMismatchException;
public class PDSACW {

    public static void main(String[] args) {
        ToDoList toDoList = new ToDoList();
        toDoList.showMenu();
    }
    
}

class Task implements Comparable<Task> {
    private String description;
    private boolean isCompleted;
    private int priority;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean startNotified;
    private boolean endNotified;
    private String notes;

    public Task(String description) {
        this.description = description;
        this.isCompleted = false;
        this.priority = Integer.MAX_VALUE; // Default priority, lower values are higher priority
        this.startTime = null;
        this.endTime = null;
        this.startNotified = false;
        this.endNotified = false;
        this.notes = "";
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void markCompleted() {
        isCompleted = true;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public boolean isStartNotified() {
        return startNotified;
    }

    public void setStartNotified(boolean startNotified) {
        this.startNotified = startNotified;
    }

    public boolean isEndNotified() {
        return endNotified;
    }

    public void setEndNotified(boolean endNotified) {
        this.endNotified = endNotified;
    }

    public String getNotes() {
        return notes;
    }

    public void addNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        String due = (startTime != null && endTime != null) ? " [Due: " + startTime + " to " + endTime + "]" : "";
        return description + (isCompleted ? " (Completed)" : "") + " [Priority: " + priority + "]" + due + " [Notes: " + notes + "]";
    }

    @Override
    public int compareTo(Task other) {
        return Integer.compare(this.priority, other.priority);
    }
}

class Node {
    Task task;
    Node next;

    public Node(Task task) {
        this.task = task;
        this.next = null;
    }
}

class SinglyLinkedList {
     Node head;
     Node tail;

    public SinglyLinkedList() {
        head = null;
        tail = null;
    }

    public void add(Task task) {
        Node newNode = new Node(task);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
    }

    public void remove(int index) {
        if (head == null || index < 0) {
            System.out.println("Invalid task index.");
            return;
        }

        if (index == 0) {
            head = head.next;
            if (head == null) {
                tail = null;
            }
            return;
        }

        Node current = head;
        Node previous = null;
        int currentIndex = 0;

        while (current != null && currentIndex < index) {
            previous = current;
            current = current.next;
            currentIndex++;
        }

        if (current != null) {
            previous.next = current.next;
            if (current.next == null) {
                tail = previous;
            }
        } else {
            System.out.println("Invalid task index.");
        }
    }

    public Task get(int index) {
        Node current = head;
        int currentIndex = 0;

        while (current != null && currentIndex < index) {
            current = current.next;
            currentIndex++;
        }

        return (current != null) ? current.task : null;
    }

    public int size() {
        int size = 0;
        Node current = head;

        while (current != null) {
            size++;
            current = current.next;
        }

        return size;
    }

    public void printList() {
        Node current = head;
        int index = 0;

        while (current != null) {
            System.out.println((index + 1) + ". " + current.task);
            current = current.next;
            index++;
        }
    }
}

class ToDoList {
    private SinglyLinkedList tasks;
    private SinglyLinkedList missedTasks;

    public ToDoList() {
        tasks = new SinglyLinkedList();
        missedTasks = new SinglyLinkedList();
        startNotificationThread();
    }

    public void addTask(String description) {
        tasks.add(new Task(description));
    }

    public void removeTask(int index) {
        tasks.remove(index);
    }

    public void viewTasks() {
        System.out.println("To-Do List:");
        tasks.printList();
    }

    public void viewCompletedTasks() {
        System.out.println("Completed Tasks:");
        Node current = tasks.head;
        int index = 0;

        while (current != null) {
            if (current.task.isCompleted()) {
                System.out.println((index + 1) + ". " + current.task);
            }
            current = current.next;
            index++;
        }
    }

    public void markTaskCompleted(int index) {
        Task task = tasks.get(index);
        if (task != null) {
            task.markCompleted();
        } else {
            System.out.println("Invalid task index.");
        }
    }

    public void setPriority(int index, int priority) {
        Task task = tasks.get(index);
        if (task != null) {
            task.setPriority(priority);
        } else {
            System.out.println("Invalid task index.");
        }
    }

    public void removePriority(int index) {
        Task task = tasks.get(index);
        if (task != null) {
            task.setPriority(Integer.MAX_VALUE);
        } else {
            System.out.println("Invalid task index.");
        }
    }

    public void setDue(int index, LocalDateTime startTime, LocalDateTime endTime) {
        Task task = tasks.get(index);
        if (task != null && !task.isCompleted()) {
            if (isTimeSlotAvailable(startTime, endTime)) {
                task.setStartTime(startTime);
                task.setEndTime(endTime);
                System.out.println("Due time set for task \"" + task.getDescription() + "\" from " + startTime + " to " + endTime);
            } else {
                System.out.println("Time slot is not available.");
            }
        } else {
            System.out.println("Invalid task index or task is already completed.");
        }
    }

    private boolean isTimeSlotAvailable(LocalDateTime startTime, LocalDateTime endTime) {
        Node current = tasks.head;

        while (current != null) {
            Task task = current.task;
            if (task.getStartTime() != null && task.getEndTime() != null) {
                if (startTime.isBefore(task.getEndTime()) && endTime.isAfter(task.getStartTime())) {
                    return false;
                }
            }
            current = current.next;
        }
        return true;
    }

    private void startNotificationThread() {
        Thread notificationThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000); // Check every second
                    LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
                    Node current = tasks.head;

                    while (current != null) {
                        Task task = current.task;
                        if (task.getStartTime() != null && !task.isStartNotified() && now.equals(task.getStartTime().truncatedTo(ChronoUnit.MINUTES))) {
                            System.out.println("Your task \"" + task.getDescription() + "\" is starting.");
                            task.setStartNotified(true);
                        }
                        if (task.getEndTime() != null && !task.isEndNotified() && now.equals(task.getEndTime().truncatedTo(ChronoUnit.MINUTES))) {
                            System.out.println("Your task \"" + task.getDescription() + "\" is ending.");
                            task.setEndNotified(true);
                        }
                        current = current.next;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        notificationThread.setDaemon(true);
        notificationThread.start();
    }

    public void addNotes(int index, String notes) {
        Task task = tasks.get(index);
        if (task != null) {
            task.addNotes(notes);
        } else {
            System.out.println("Invalid task index.");
        }
    }

    public void showMenu() {
    Scanner scanner = new Scanner(System.in);

    while (true) {
        System.out.println("\nMenu:");
        System.out.println("1. Add task");
        System.out.println("2. Remove task");
        System.out.println("3. View tasks");
        System.out.println("4. Mark task completed");
        System.out.println("5. Set task priority");
        System.out.println("6. Remove task priority");
        System.out.println("7. Set task due time");
        System.out.println("8. Add notes to task");
        System.out.println("9. View completed tasks");
        System.out.println("10. Check and view missed tasks");
        System.out.println("11. Exit");
        System.out.print("Choose an option: ");

        int choice;
        try {
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.nextLine(); // Consume invalid input
            continue;
        }

        switch (choice) {
            case 1:
                System.out.print("Enter task description: ");
                String description = scanner.nextLine();
                addTask(description);
                break;
            case 2:
                System.out.print("Enter task index to remove: ");
                int removeIndex = scanner.nextInt() - 1;
                scanner.nextLine(); // Consume newline
                removeTask(removeIndex);
                break;
            case 3:
                viewTasks();
                break;
            case 4:
                System.out.print("Enter task index to mark as completed: ");
                int completeIndex = scanner.nextInt() - 1;
                scanner.nextLine(); // Consume newline
                markTaskCompleted(completeIndex);
                break;
            case 5:
                System.out.print("Enter task index to set priority: ");
                int priorityIndex = scanner.nextInt() - 1;
                System.out.print("Enter priority (lower value means higher priority): ");
                int priority = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                setPriority(priorityIndex, priority);
                break;
            case 6:
                System.out.print("Enter task index to remove priority: ");
                int removePriorityIndex = scanner.nextInt() - 1;
                scanner.nextLine(); // Consume newline
                removePriority(removePriorityIndex);
                break;
            case 7:
                System.out.print("Enter task index to set due time: ");
                int dueIndex = scanner.nextInt() - 1;
                scanner.nextLine(); // Consume newline
                System.out.print("Enter start time (yyyy-MM-dd HH:mm): ");
                String startTimeStr = scanner.nextLine();
                System.out.print("Enter end time (yyyy-MM-dd HH:mm): ");
                String endTimeStr = scanner.nextLine();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);
                LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);
                setDue(dueIndex, startTime, endTime);
                break;
            case 8:
                System.out.print("Enter task index to add notes: ");
                int notesIndex = scanner.nextInt() - 1;
                scanner.nextLine(); // Consume newline
                System.out.print("Enter notes: ");
                String notes = scanner.nextLine();
                addNotes(notesIndex, notes);
                break;
            case 9:
                viewCompletedTasks();
                break;
            case 10:
                checkMissedTasks();
                viewMissedTasks();
                break;
            case 11:
                System.out.println("Exiting...");
                return;
            default:
                System.out.println("Invalid option. Please try again.");
                break;
        }
    }
}

    public void checkMissedTasks() {
    LocalDateTime now = LocalDateTime.now();
    Node current = tasks.head;
    Node previous = null;

    while (current != null) {
        Task task = current.task;
        if (task.getEndTime() != null && now.isAfter(task.getEndTime()) && !task.isCompleted()) {
            // Move the task to missedTasks
            missedTasks.add(task);

            // Remove the task from tasks
            if (previous == null) {
                tasks.head = current.next;
            } else {
                previous.next = current.next;
            }
        } else {
            previous = current;
        }
        current = current.next;
    }
}
    public void viewMissedTasks() {
    System.out.println("Missed Tasks:");
    missedTasks.printList();
}


}