import java.util.Scanner;
import java.util.Queue;
import java.util.LinkedList;
public class Scheduling {
static class Process {
int pid, at, bt, pr;
int ct, tat, wt, rem;
Process(int pid, int at, int bt, int pr) {
this.pid = pid;
this.at = at;
this.bt = bt;
this.pr = pr;
this.rem = bt;
}
}
static final int MAX = 20;
static void printResult(Process[] p, int n) {
double totalWT = 0, totalTAT = 0;
System.out.println("\nPID\tAT\tBT\tCT\tTAT\tWT");
for (int i = 0; i < n; i++) {

  System.out.printf("%d\t%d\t%d\t%d\t%d\t%d%n",
  p[i].pid, p[i].at, p[i].bt,
  p[i].ct, p[i].tat, p[i].wt);
  totalWT += p[i].wt;
  totalTAT += p[i].tat;
}
System.out.printf("Average Waiting Time = %.2f%n",
totalWT / n);
System.out.printf("Average Turnaround Time = %.2f%n",
totalTAT / n);
}
static void calculate(Process p) {
p.tat = p.ct - p.at;
p.wt = p.tat - p.bt;
}
// FCFS Scheduling
static void fcfs(Process[] p, int n) {
  int time = 0;
  boolean[] done = new boolean[n];
  for (int count = 0; count < n; count++) {
    int idx = -1;
    int minAT = Integer.MAX_VALUE;
    for (int i = 0; i < n; i++) {
    if (!done[i] && p[i].at < minAT) {
    minAT = p[i].at;
    idx = i;
  }
}
if (time < p[idx].at)
time = p[idx].at;
time += p[idx].bt;
p[idx].ct = time;
calculate(p[idx]);
done[idx] = true;
}
printResult(p, n);
}
// SJF Non-Preemptive Scheduling
static void sjf(Process[] p, int n) {
int completed = 0, time = 0;
boolean[] done = new boolean[n];
while (completed < n) {
int idx = -1;
int minBT = Integer.MAX_VALUE;
for (int i = 0; i < n; i++) {
if (!done[i] && p[i].at <= time
&& p[i].bt < minBT) {
minBT = p[i].bt;
idx = i;
}
}
if (idx == -1) {
time++;
continue;
}
time += p[idx].bt;
p[idx].ct = time;
calculate(p[idx]);
done[idx] = true;
completed++;
}
printResult(p, n);
}
// Priority Non-Preemptive Scheduling
static void priorityScheduling(Process[] p, int n) {
int completed = 0, time = 0;
boolean[] done = new boolean[n];
while (completed < n) {
int idx = -1;
int best = Integer.MAX_VALUE;
for (int i = 0; i < n; i++) {
if (!done[i] && p[i].at <= time
&& p[i].pr < best) {
best = p[i].pr;
idx = i;
}

}
if (idx == -1) {
time++;
continue;
}
time += p[idx].bt;
p[idx].ct = time;
calculate(p[idx]);
done[idx] = true;
completed++;
}
printResult(p, n);
}
// Round Robin Scheduling
static void roundRobin(Process[] p, int n, int quantum) {
int time = 0, completed = 0;
Queue<Integer> queue = new LinkedList<>();
boolean[] inQueue = new boolean[n];
for (int i = 0; i < n; i++) {
p[i].rem = p[i].bt;
if (p[i].at <= time) {
queue.add(i);
inQueue[i] = true;
}
}
while (completed < n) {
if (queue.isEmpty()) {
time++;
for (int i = 0; i < n; i++) {
if (!inQueue[i] && p[i].rem > 0
&& p[i].at <= time) {
queue.add(i);
inQueue[i] = true;
}
}
continue;
}

int idx = queue.poll();
inQueue[idx] = false;
int slice = Math.min(p[idx].rem, quantum);
time += slice;
p[idx].rem -= slice;
for (int i = 0; i < n; i++) {
if (!inQueue[i] && p[i].rem > 0
&& p[i].at <= time && i != idx) {
queue.add(i);
inQueue[i] = true;
}
}
if (p[idx].rem > 0) {
queue.add(idx);
inQueue[idx] = true;
} else {
p[idx].ct = time;
calculate(p[idx]);
completed++;
}
}
printResult(p, n);
}
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n, choice, quantum;
Process[] p = new Process[MAX];
System.out.print("Enter number of processes: ");
n = sc.nextInt();
if (n < 1 || n > MAX) {
System.out.println("Invalid number of processes");
sc.close();
return;
}
for (int i = 0; i < n; i++) {
System.out.print("Process " + (i + 1)

+ " - Arrival Time, Burst Time, Priority: ");
int at = sc.nextInt();
int bt = sc.nextInt();
int pr = sc.nextInt();
if (at < 0 || bt <= 0) {
System.out.println("Invalid process details");
sc.close();
return;
}
p[i] = new Process(i + 1, at, bt, pr);
}
System.out.println("\nMenu:");
System.out.println("1. FCFS");
System.out.println("2. SJF (Non-preemptive)");
System.out.println("3. Priority (Non-preemptive)");
System.out.println("4. Round Robin");
System.out.print("Enter choice: ");
choice = sc.nextInt();
switch (choice) {
case 1:
fcfs(p, n);
break;
case 2:
sjf(p, n);
break;
case 3:
priorityScheduling(p, n);
break;
case 4:
System.out.print("Enter time quantum: ");
quantum = sc.nextInt();
if (quantum <= 0) {
System.out.println("Invalid time quantum");
break;
}
roundRobin(p, n, quantum);
break;
default:
System.out.println("Invalid choice");
}
sc.close();
}
}
