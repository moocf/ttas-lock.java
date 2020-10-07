import java.util.concurrent.atomic.*;

// Test-test-and-set Lock uses an atomic value for
// indicating that some thread has engaged the lock
// and is executing its critical section (CS).
// 
// Each thread that wants to enter CS waits until
// the atomic lock is disengaged. Then it tries
// to reengage it, but as several threads might
// be trying the same, it may or may not succeed.
// So it checks if it succeeded (with the same
// atomic operation), and if so its done. If not
// it retries all over again.
// 
// Once the thread is done with CS, it simply
// disengages the lock.
// 
// As all thread wait (spin) for the lock to
// disengage at the same time, and try reengaging
// it only when they see it as unlocked, unnecessary
// bus traffic (storm) is reduced. However, this
// does not provide first-come-first-served
// fairness, or efficient cache usage. Since
// it only uses a single atomic value per lock
// it is only suitable for low-contention
// memory-limited architectures.

class TTASLock extends AbstractLock {
  AtomicBoolean locked;
  // locked: indicates if lock is engaged

  public TTASLock() {
    locked = new AtomicBoolean(false);
  }

  // 1. When thread wants to access critical
  //    section, it checks to see if lock is already
  //    engaged, and if so, waits (spins).
  // 2. Once lock is disengaged it tries to reengage
  //    it. So do all other threads wanting to enter
  //    CS. Its a race between threads. So this
  //    thread checks to see it was the one who was
  //    successful, and if so its done.
  // 3. If not, it retries again.
  @Override
  public void lock() {
    while(true) {                         // 1
      while(locked.get()) Thread.yield(); // 1
      if(!locked.getAndSet(true)) return; // 2
    } // 3
  }

  // 1. When a thread is done with its critical
  //    section, it simply sets the "locked" state
  //    to false.
  @Override
  public void unlock() {
    locked.set(false); // 1
  }
}
