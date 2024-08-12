package pdsa.cw;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;
import java.util.InputMismatchException;
public class PDSACW {
//main class
    public static void main(String[] args) {
        ToDoList toDoList = new ToDoList();
        toDoList.showMenu();//this is the code which call the menu  
    }
    
}

class Task implements Comparable<Task> {// comparable this is used for the perpose of sorting the data
    private String description;
    private boolean isCompleted;
    private int priority;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean startNotified;
    private boolean endNotified;
    private String notes;

    public Task(String description) {// this Constructor used to set  initial values 
        this.description = description;
        this.isCompleted = false;
        this.priority = Integer.MAX_VALUE; // default priority, lower values are higher priority
        this.startTime = null;
        this.endTime = null;
        this.startNotified = false;
        this.endNotified = false;
        this.notes = "";
    }
  //these are getters
    public String getDescription() {
        //gives the descripition when ever we need
        return description;
    }

    public boolean isCompleted() {
        //used for check where the task is completed
        return isCompleted;
    }

    public int getPriority() {
        //the see the tasks priority level
        return priority;
    }
    public LocalDateTime getStartTime() {
        // to get the start time of the task
        return startTime;
    }
     public LocalDateTime getEndTime() {
        //get the end time of a task
        return endTime;
    }
     public boolean isStartNotified() {
        //to check where the start notification is send
        return startNotified;
    }
     public String getNotes() {
        //used to get the notes for the task
        return notes;
    }
     public boolean isEndNotified() {
        //to check where the end notification is send
        return endNotified;
    }
     
     
     //these are the setters
     
    public void setPriority(int priority) {
        // to set priority for the tasks
        this.priority = priority;
    }

    public void markCompleted() {
        // to mark the task completed
        isCompleted = true;
    }

    public void setStartTime(LocalDateTime startTime) {
        //when we need to set start time 
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        // to set the end time for a task
        this.endTime = endTime;
    }

    public void setStartNotified(boolean startNotified) {
        // to see whether a start noti has been sended
        this.startNotified = startNotified;
    }

    public void setEndNotified(boolean endNotified) {
        // to see whether a end noti has been sended
        this.endNotified = endNotified;
    }

    public void addNotes(String notes) {
        //to add notes for the task
        this.notes = notes;
    }
    
    
     //returns string values to the task
    @Override
    public String toString() {
    String due = (startTime != null && endTime != null) ? " [Due: " + startTime + " to " + endTime + "]" : "";
    String priorityStr = (priority != Integer.MAX_VALUE) ? " [Priority: " + priority + "]" : "";
    String notesStr = (!notes.isEmpty()) ? " [Notes: " + notes + "]" : "";
    return description + (isCompleted ? " (Completed)" : "") + priorityStr + due + notesStr;
}

  // compares tasks based on priority sorting
    @Override
    public int compareTo(Task other) {
        return Integer.compare(this.priority, other.priority);
    }
}

class Node {
    //Node class where nodes are created
    Task task;//task 
    Node next;//location for the next node

    public Node(Task task) {
        this.task = task;
        this.next = null;
    }
}

class SinglyLinkedList {
     Node head;// represtents the first node of the list 
     Node tail;// represtents the last node of the list 

    public SinglyLinkedList() {
        head = null;
        tail = null;
    }
//Task task = new Task();
    public void add(Task task) {
        Node newNode = new Node(task);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;//making the new node as the last one
            tail = newNode;//make the new tail of the list
        }
    }

    public void remove(int index) {
        //to remove task
        if (head == null || index < 0) {// checking if the list is empty or index is lessthan 0
            System.out.println("Invalid task index.");
            return;
        }
//to remove the first task
        if (index == 0) {//checking here te if tyhe user want to remove the first node
            head = head.next;//updates the head to point to the next node in the list
           
            if (head == null) {// updating the tail also null if there is only one node
                tail = null;
            }
            return;
        }
// to remove a selected index
        Node current = head;
        Node previous = null;
        int currentIndex = 0;

        while (current != null && currentIndex < index) {
            previous = current;// before the current node
            current = current.next;//current node
            currentIndex++;//to tack the position of the list
        }

        if (current != null) {
            previous.next = current.next;
            if (current.next == null) {
                tail = previous;
            }
        } else {//if the index  is large than the size
            System.out.println("Invalid task index.");
        }
    }

    public Task get(int index) {
        //get tasks based on the index like search
        Node current = head;
        int currentIndex = 0;

        while (current != null && currentIndex < index) {
            current = current.next;
            currentIndex++;
        }

        return (current != null) ? current.task : null;
    }

    public int size() {
        // to get the number of task in the list
        int size = 0;
        Node current = head;

        while (current != null) {
            size++;
            current = current.next;
        }

        return size;
    }

    public void printList() {
        // to print the tasks
        Node current = head;
        int index = 0;

        while (current != null) {
            System.out.println((index + 1) + ". " + current.task);
            current = current.next;
            index++;
        }
    }
      // Method to sort the list based on priority
    public void sort() {
    if (head == null || head.next == null) {
        return;
    }

    Node sorted = null;

    while (head != null) {
        Node current = head;
        head = head.next;

        if (sorted == null || current.task.getPriority() < sorted.task.getPriority()) {
            current.next = sorted;
            sorted = current;
        } else {
            Node temp = sorted;
            while (temp.next != null && temp.next.task.getPriority() <= current.task.getPriority()) {
                temp = temp.next;
            }
            current.next = temp.next;
            temp.next = current;
        }
    }

    head = sorted;

    // Reset tail
    Node temp = head;
    while (temp.next != null) {
        temp = temp.next;
    }
    tail = temp;
}

}


class ToDoList {
    private SinglyLinkedList tasks;//here is a linkedlist for tasks
    private SinglyLinkedList missedTasks;//here is a linked list for missed task

    public ToDoList() {
        tasks = new SinglyLinkedList();//empty object creation
        missedTasks = new SinglyLinkedList();
        startNotificationThread();//this method is called to begin a background threading which notify the strt and end time of a task
    }

    public void addTask(String description) {
        tasks.add(new Task(description));//calling add method which is in the singlylinked list class
        System.out.println("Task Added Successfully");
    }

    public void removeTask(int index) {
        tasks.remove(index);//calling the remove method in singlylinked list
        System.out.println("Task Removed Successfully");
    }

    public void viewTasks() {
    System.out.println("To-Do List:");
    Node current = tasks.head;
    int index = 0;

    while (current != null) {
        if (!current.task.isCompleted()) {
            System.out.println((index + 1) + ". " + current.task);
        }
        current = current.next;
        index++;
    }
}


    public void viewCompletedTasks() {
        System.out.println("Completed Tasks:");
        Node current = tasks.head;
        int index = 0;

        while (current != null) {
            if (current.task.isCompleted()) {
                System.out.println((index + 1) + ". " + current.task);//index numbers
            }
            current = current.next;
            index++;
        }
    }

    public void markTaskCompleted(int index) {
        Task task = tasks.get(index);
        if (task != null) {
            task.markCompleted();
            System.out.println("Task Mark As Completed Successfully");
        } else {
            System.out.println("Invalid task index.");
        }
    }

    public void setPriority(int index, int priority) {
        Task task = tasks.get(index);
        if (task != null) {
            task.setPriority(priority);
            tasks.sort();
            System.out.println("Task Prioritized Successfully");
            // Sort the tasks after setting a priority
        } else {
            System.out.println("Invalid task index.");
        }
    }

    public void removePriority(int index) {
        Task task = tasks.get(index);
    if (task != null) {
        task.setPriority(Integer.MAX_VALUE); // Reset to default priority
        
        // Temporarily remove the task from the list
        tasks.remove(index);
        
        // Re-add the task to move it to the end
        tasks.add(task);
        
        // Sort the list again to ensure it stays in the correct order
        tasks.sort(); 
        System.out.println("Remove Task Prioriti Successfully");
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
            System.out.println("Note Added Successfully");
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
            scanner.nextLine(); // gets the index as input
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.nextLine(); // gets the index if there is any error
            continue;
        }

        switch (choice) {
            case 1://for add task takes input
                System.out.print("Enter task description: ");
                String description = scanner.nextLine();
                addTask(description);
                break;
            case 2://
                System.out.print("Enter task index to remove: ");
                int removeIndex = scanner.nextInt() - 1;//gets intput and the nsame time reduse 1 from the input
                scanner.nextLine(); //
                removeTask(removeIndex);//calls the remove task and gives the index to remove
                break;
            case 3:
                viewTasks();// calling the view method
                break;
            case 4:
                System.out.print("Enter task index to mark as completed: ");
                int completeIndex = scanner.nextInt() - 1;//takes input and reduse one index
                scanner.nextLine(); 
                markTaskCompleted(completeIndex);//calls the mark completed tasks and passes the parameter 
                break;
            case 5:
                System.out.print("Enter task index to set priority: ");
                int priorityIndex = scanner.nextInt() - 1;//gets the task which should be priorities and reduses one digit
                System.out.print("Enter priority (lower value means higher priority): ");
                int priority = scanner.nextInt();//gets the pririty number
                scanner.nextLine();
                setPriority(priorityIndex, priority);//pass the both index to set priority
                break;
            case 6:
                System.out.print("Enter task index to remove priority: ");
                int removePriorityIndex = scanner.nextInt() - 1;
                scanner.nextLine(); 
                removePriority(removePriorityIndex);//calls the remove priority method and pass the index which priority must be removed
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
                System.out.println("Exiting...");// these h=is for exting
                return;
            default:
                System.out.println("Invalid option. Please try again.");// is we enter any other than the need index 
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
    missedTasks.printList();//calling print method in singlylist
}


}
