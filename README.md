Test-test-and-set Lock uses an atomic value for
indicating that some thread has engaged the lock
and is executing its critical section (CS).

Each thread that wants to enter CS waits until
the atomic lock is disengaged. Then it tries
to reengage it, but as several threads might
be trying the same, it may or may not succeed.
So it checks if it succeeded (with the same
atomic operation), and if so its done. If not
it retries all over again.

Once the thread is done with CS, it simply
disengages the lock.

As all thread wait (spin) for the lock to
disengage at the same time, and try reengaging
it only when they see it as unlocked, unnecessary
bus traffic (storm) is reduced. However, this
does not provide first-come-first-served
fairness, or efficient cache usage. Since
it only uses a single atomic value per lock
it is only suitable for low-contention
memory-limited architectures.

The TTASLock is due to [Clyde Kruskal],
[Larry Rudolph], and [Marc Snir].

[Clyde Kruskal]: https://en.wikipedia.org/wiki/Clyde_Kruskal
[Larry Rudolph]: http://people.csail.mit.edu/rudolph/
[Marc Snir]: https://en.wikipedia.org/wiki/Marc_Snir

> **Course**: [Concurrent Data Structures], Monsoon 2020\
> **Taught by**: Prof. Govindarajulu Regeti

[Concurrent Data Structures]: https://github.com/iiithf/concurrent-data-structures

```java
lock():
1. When thread wants to access critical
   section, it checks to see if lock is already
   engaged, and if so, waits (spins).
2. Once lock is disengaged it tries to reengage
   it. So do all other threads wanting to enter
   CS. Its a race between threads. So this
   thread checks to see it was the one who was
   successful, and if so its done.
3. If not, it retries again.
```

```java
unlock():
1. When a thread is done with its critical
   section, it simply sets the "locked" state
   to false.
```

See [TTASLock.java] for code, [Main.java] for test, and [repl.it] for output.

[TTASLock.java]: https://repl.it/@wolfram77/ttas-lock#TTASLock.java
[Main.java]: https://repl.it/@wolfram77/ttas-lock#Main.java
[repl.it]: https://ttas-lock.wolfram77.repl.run


### references

- [The Art of Multiprocessor Programming :: Maurice Herlihy, Nir Shavit](https://dl.acm.org/doi/book/10.5555/2385452)
- [Efficient synchronization of multiprocessors with shared memory :: Clyde Kruskal, Larry Rudolph, Marc Snir](https://dl.acm.org/doi/10.1145/48022.48024)

![](https://ga-beacon.deno.dev/G-G1E8HNDZYY:v51jklKGTLmC3LAZ4rJbIQ/github.com/javaf/ttas-lock)
