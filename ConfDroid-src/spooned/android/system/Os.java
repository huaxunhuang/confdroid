/**
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.system;


/**
 * Access to low-level system functionality. Most of these are system calls. Most users will want
 * to use higher-level APIs where available, but this class provides access to the underlying
 * primitives used to implement the higher-level APIs.
 *
 * <p>The corresponding constants can be found in {@link OsConstants}.
 */
public final class Os {
    private Os() {
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/accept.2.html">accept(2)</a>.
     */
    public static java.io.FileDescriptor accept(java.io.FileDescriptor fd, java.net.InetSocketAddress peerAddress) throws android.system.ErrnoException, java.net.SocketException {
        return Libcore.os.accept(fd, peerAddress);
    }

    /**
     * TODO Change the public API by removing the overload above and unhiding this version.
     *
     * @unknown 
     */
    public static java.io.FileDescriptor accept(java.io.FileDescriptor fd, java.net.SocketAddress peerAddress) throws android.system.ErrnoException, java.net.SocketException {
        return Libcore.os.accept(fd, peerAddress);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/access.2.html">access(2)</a>.
     */
    public static boolean access(java.lang.String path, int mode) throws android.system.ErrnoException {
        return Libcore.os.access(path, mode);
    }

    /**
     *
     *
     * @unknown 
     */
    public static java.net.InetAddress[] android_getaddrinfo(java.lang.String node, android.system.StructAddrinfo hints, int netId) throws android.system.GaiException {
        return Libcore.os.android_getaddrinfo(node, hints, netId);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/bind.2.html">bind(2)</a>.
     */
    public static void bind(java.io.FileDescriptor fd, java.net.InetAddress address, int port) throws android.system.ErrnoException, java.net.SocketException {
        Libcore.os.bind(fd, address, port);
    }

    /**
     *
     *
     * @unknown 
     */
    public static void bind(java.io.FileDescriptor fd, java.net.SocketAddress address) throws android.system.ErrnoException, java.net.SocketException {
        Libcore.os.bind(fd, address);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/chmod.2.html">chmod(2)</a>.
     */
    public static void chmod(java.lang.String path, int mode) throws android.system.ErrnoException {
        Libcore.os.chmod(path, mode);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/chown.2.html">chown(2)</a>.
     */
    public static void chown(java.lang.String path, int uid, int gid) throws android.system.ErrnoException {
        Libcore.os.chown(path, uid, gid);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/close.2.html">close(2)</a>.
     */
    public static void close(java.io.FileDescriptor fd) throws android.system.ErrnoException {
        Libcore.os.close(fd);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/connect.2.html">connect(2)</a>.
     */
    public static void connect(java.io.FileDescriptor fd, java.net.InetAddress address, int port) throws android.system.ErrnoException, java.net.SocketException {
        Libcore.os.connect(fd, address, port);
    }

    /**
     *
     *
     * @unknown 
     */
    public static void connect(java.io.FileDescriptor fd, java.net.SocketAddress address) throws android.system.ErrnoException, java.net.SocketException {
        Libcore.os.connect(fd, address);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/dup.2.html">dup(2)</a>.
     */
    public static java.io.FileDescriptor dup(java.io.FileDescriptor oldFd) throws android.system.ErrnoException {
        return Libcore.os.dup(oldFd);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/dup2.2.html">dup2(2)</a>.
     */
    public static java.io.FileDescriptor dup2(java.io.FileDescriptor oldFd, int newFd) throws android.system.ErrnoException {
        return Libcore.os.dup2(oldFd, newFd);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man3/environ.3.html">environ(3)</a>.
     */
    public static java.lang.String[] environ() {
        return Libcore.os.environ();
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/execv.2.html">execv(2)</a>.
     */
    public static void execv(java.lang.String filename, java.lang.String[] argv) throws android.system.ErrnoException {
        Libcore.os.execv(filename, argv);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/execve.2.html">execve(2)</a>.
     */
    public static void execve(java.lang.String filename, java.lang.String[] argv, java.lang.String[] envp) throws android.system.ErrnoException {
        Libcore.os.execve(filename, argv, envp);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/fchmod.2.html">fchmod(2)</a>.
     */
    public static void fchmod(java.io.FileDescriptor fd, int mode) throws android.system.ErrnoException {
        Libcore.os.fchmod(fd, mode);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/fchown.2.html">fchown(2)</a>.
     */
    public static void fchown(java.io.FileDescriptor fd, int uid, int gid) throws android.system.ErrnoException {
        Libcore.os.fchown(fd, uid, gid);
    }

    /**
     *
     *
     * @unknown 
     */
    public static int fcntlFlock(java.io.FileDescriptor fd, int cmd, android.system.StructFlock arg) throws android.system.ErrnoException, java.io.InterruptedIOException {
        return Libcore.os.fcntlFlock(fd, cmd, arg);
    }

    /**
     *
     *
     * @unknown 
     */
    public static int fcntlInt(java.io.FileDescriptor fd, int cmd, int arg) throws android.system.ErrnoException {
        return Libcore.os.fcntlInt(fd, cmd, arg);
    }

    /**
     *
     *
     * @unknown 
     */
    public static int fcntlVoid(java.io.FileDescriptor fd, int cmd) throws android.system.ErrnoException {
        return Libcore.os.fcntlVoid(fd, cmd);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/fdatasync.2.html">fdatasync(2)</a>.
     */
    public static void fdatasync(java.io.FileDescriptor fd) throws android.system.ErrnoException {
        Libcore.os.fdatasync(fd);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/fstat.2.html">fstat(2)</a>.
     */
    public static android.system.StructStat fstat(java.io.FileDescriptor fd) throws android.system.ErrnoException {
        return Libcore.os.fstat(fd);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/fstatvfs.2.html">fstatvfs(2)</a>.
     */
    public static android.system.StructStatVfs fstatvfs(java.io.FileDescriptor fd) throws android.system.ErrnoException {
        return Libcore.os.fstatvfs(fd);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/fsync.2.html">fsync(2)</a>.
     */
    public static void fsync(java.io.FileDescriptor fd) throws android.system.ErrnoException {
        Libcore.os.fsync(fd);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/ftruncate.2.html">ftruncate(2)</a>.
     */
    public static void ftruncate(java.io.FileDescriptor fd, long length) throws android.system.ErrnoException {
        Libcore.os.ftruncate(fd, length);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man3/gai_strerror.3.html">gai_strerror(3)</a>.
     */
    public static java.lang.String gai_strerror(int error) {
        return Libcore.os.gai_strerror(error);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/getegid.2.html">getegid(2)</a>.
     */
    public static int getegid() {
        return Libcore.os.getegid();
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/geteuid.2.html">geteuid(2)</a>.
     */
    public static int geteuid() {
        return Libcore.os.geteuid();
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/getgid.2.html">getgid(2)</a>.
     */
    public static int getgid() {
        return Libcore.os.getgid();
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man3/getenv.3.html">getenv(3)</a>.
     */
    public static java.lang.String getenv(java.lang.String name) {
        return Libcore.os.getenv(name);
    }

    /**
     *
     *
     * @unknown 
     */
    public static java.lang.String getnameinfo(java.net.InetAddress address, int flags) throws android.system.GaiException {
        return Libcore.os.getnameinfo(address, flags);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/getpeername.2.html">getpeername(2)</a>.
     */
    public static java.net.SocketAddress getpeername(java.io.FileDescriptor fd) throws android.system.ErrnoException {
        return Libcore.os.getpeername(fd);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/getpgid.2.html">getpgid(2)</a>.
     */
    /**
     *
     *
     * @unknown 
     */
    public static int getpgid(int pid) throws android.system.ErrnoException {
        return Libcore.os.getpgid(pid);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/getpid.2.html">getpid(2)</a>.
     */
    public static int getpid() {
        return Libcore.os.getpid();
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/getppid.2.html">getppid(2)</a>.
     */
    public static int getppid() {
        return Libcore.os.getppid();
    }

    /**
     *
     *
     * @unknown 
     */
    public static android.system.StructPasswd getpwnam(java.lang.String name) throws android.system.ErrnoException {
        return Libcore.os.getpwnam(name);
    }

    /**
     *
     *
     * @unknown 
     */
    public static android.system.StructPasswd getpwuid(int uid) throws android.system.ErrnoException {
        return Libcore.os.getpwuid(uid);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/getsockname.2.html">getsockname(2)</a>.
     */
    public static java.net.SocketAddress getsockname(java.io.FileDescriptor fd) throws android.system.ErrnoException {
        return Libcore.os.getsockname(fd);
    }

    /**
     *
     *
     * @unknown 
     */
    public static int getsockoptByte(java.io.FileDescriptor fd, int level, int option) throws android.system.ErrnoException {
        return Libcore.os.getsockoptByte(fd, level, option);
    }

    /**
     *
     *
     * @unknown 
     */
    public static java.net.InetAddress getsockoptInAddr(java.io.FileDescriptor fd, int level, int option) throws android.system.ErrnoException {
        return Libcore.os.getsockoptInAddr(fd, level, option);
    }

    /**
     *
     *
     * @unknown 
     */
    public static int getsockoptInt(java.io.FileDescriptor fd, int level, int option) throws android.system.ErrnoException {
        return Libcore.os.getsockoptInt(fd, level, option);
    }

    /**
     *
     *
     * @unknown 
     */
    public static android.system.StructLinger getsockoptLinger(java.io.FileDescriptor fd, int level, int option) throws android.system.ErrnoException {
        return Libcore.os.getsockoptLinger(fd, level, option);
    }

    /**
     *
     *
     * @unknown 
     */
    public static android.system.StructTimeval getsockoptTimeval(java.io.FileDescriptor fd, int level, int option) throws android.system.ErrnoException {
        return Libcore.os.getsockoptTimeval(fd, level, option);
    }

    /**
     *
     *
     * @unknown 
     */
    public static android.system.StructUcred getsockoptUcred(java.io.FileDescriptor fd, int level, int option) throws android.system.ErrnoException {
        return Libcore.os.getsockoptUcred(fd, level, option);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/gettid.2.html">gettid(2)</a>.
     */
    public static int gettid() {
        return Libcore.os.gettid();
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/getuid.2.html">getuid(2)</a>.
     */
    public static int getuid() {
        return Libcore.os.getuid();
    }

    /**
     *
     *
     * @unknown 
     */
    public static int getxattr(java.lang.String path, java.lang.String name, byte[] outValue) throws android.system.ErrnoException {
        return Libcore.os.getxattr(path, name, outValue);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man3/if_indextoname.3.html">if_indextoname(3)</a>.
     */
    public static java.lang.String if_indextoname(int index) {
        return Libcore.os.if_indextoname(index);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man3/inet_pton.3.html">inet_pton(3)</a>.
     */
    public static java.net.InetAddress inet_pton(int family, java.lang.String address) {
        return Libcore.os.inet_pton(family, address);
    }

    /**
     *
     *
     * @unknown 
     */
    public static java.net.InetAddress ioctlInetAddress(java.io.FileDescriptor fd, int cmd, java.lang.String interfaceName) throws android.system.ErrnoException {
        return Libcore.os.ioctlInetAddress(fd, cmd, interfaceName);
    }

    /**
     *
     *
     * @unknown 
     */
    public static int ioctlInt(java.io.FileDescriptor fd, int cmd, android.util.MutableInt arg) throws android.system.ErrnoException {
        return Libcore.os.ioctlInt(fd, cmd, arg);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man3/isatty.3.html">isatty(3)</a>.
     */
    public static boolean isatty(java.io.FileDescriptor fd) {
        return Libcore.os.isatty(fd);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/kill.2.html">kill(2)</a>.
     */
    public static void kill(int pid, int signal) throws android.system.ErrnoException {
        Libcore.os.kill(pid, signal);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/lchown.2.html">lchown(2)</a>.
     */
    public static void lchown(java.lang.String path, int uid, int gid) throws android.system.ErrnoException {
        Libcore.os.lchown(path, uid, gid);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/link.2.html">link(2)</a>.
     */
    public static void link(java.lang.String oldPath, java.lang.String newPath) throws android.system.ErrnoException {
        Libcore.os.link(oldPath, newPath);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/listen.2.html">listen(2)</a>.
     */
    public static void listen(java.io.FileDescriptor fd, int backlog) throws android.system.ErrnoException {
        Libcore.os.listen(fd, backlog);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/lseek.2.html">lseek(2)</a>.
     */
    public static long lseek(java.io.FileDescriptor fd, long offset, int whence) throws android.system.ErrnoException {
        return Libcore.os.lseek(fd, offset, whence);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/lstat.2.html">lstat(2)</a>.
     */
    public static android.system.StructStat lstat(java.lang.String path) throws android.system.ErrnoException {
        return Libcore.os.lstat(path);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/mincore.2.html">mincore(2)</a>.
     */
    public static void mincore(long address, long byteCount, byte[] vector) throws android.system.ErrnoException {
        Libcore.os.mincore(address, byteCount, vector);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/mkdir.2.html">mkdir(2)</a>.
     */
    public static void mkdir(java.lang.String path, int mode) throws android.system.ErrnoException {
        Libcore.os.mkdir(path, mode);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man3/mkfifo.3.html">mkfifo(3)</a>.
     */
    public static void mkfifo(java.lang.String path, int mode) throws android.system.ErrnoException {
        Libcore.os.mkfifo(path, mode);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/mlock.2.html">mlock(2)</a>.
     */
    public static void mlock(long address, long byteCount) throws android.system.ErrnoException {
        Libcore.os.mlock(address, byteCount);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/mmap.2.html">mmap(2)</a>.
     */
    public static long mmap(long address, long byteCount, int prot, int flags, java.io.FileDescriptor fd, long offset) throws android.system.ErrnoException {
        return Libcore.os.mmap(address, byteCount, prot, flags, fd, offset);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/msync.2.html">msync(2)</a>.
     */
    public static void msync(long address, long byteCount, int flags) throws android.system.ErrnoException {
        Libcore.os.msync(address, byteCount, flags);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/munlock.2.html">munlock(2)</a>.
     */
    public static void munlock(long address, long byteCount) throws android.system.ErrnoException {
        Libcore.os.munlock(address, byteCount);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/munmap.2.html">munmap(2)</a>.
     */
    public static void munmap(long address, long byteCount) throws android.system.ErrnoException {
        Libcore.os.munmap(address, byteCount);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/open.2.html">open(2)</a>.
     */
    public static java.io.FileDescriptor open(java.lang.String path, int flags, int mode) throws android.system.ErrnoException {
        return Libcore.os.open(path, flags, mode);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/pipe.2.html">pipe(2)</a>.
     */
    public static java.io.FileDescriptor[] pipe() throws android.system.ErrnoException {
        return Libcore.os.pipe2(0);
    }

    /**
     *
     *
     * @unknown 
     */
    public static java.io.FileDescriptor[] pipe2(int flags) throws android.system.ErrnoException {
        return Libcore.os.pipe2(flags);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/poll.2.html">poll(2)</a>.
     *
     * <p>Note that in Lollipop this could throw an {@code ErrnoException} with {@code EINTR}.
     * In later releases, the implementation will automatically just restart the system call with
     * an appropriately reduced timeout.
     */
    public static int poll(android.system.StructPollfd[] fds, int timeoutMs) throws android.system.ErrnoException {
        return Libcore.os.poll(fds, timeoutMs);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/posix_fallocate.2.html">posix_fallocate(2)</a>.
     */
    public static void posix_fallocate(java.io.FileDescriptor fd, long offset, long length) throws android.system.ErrnoException {
        Libcore.os.posix_fallocate(fd, offset, length);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/prctl.2.html">prctl(2)</a>.
     */
    public static int prctl(int option, long arg2, long arg3, long arg4, long arg5) throws android.system.ErrnoException {
        return Libcore.os.prctl(option, arg2, arg3, arg4, arg5);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/pread.2.html">pread(2)</a>.
     */
    public static int pread(java.io.FileDescriptor fd, java.nio.ByteBuffer buffer, long offset) throws android.system.ErrnoException, java.io.InterruptedIOException {
        return Libcore.os.pread(fd, buffer, offset);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/pread.2.html">pread(2)</a>.
     */
    public static int pread(java.io.FileDescriptor fd, byte[] bytes, int byteOffset, int byteCount, long offset) throws android.system.ErrnoException, java.io.InterruptedIOException {
        return Libcore.os.pread(fd, bytes, byteOffset, byteCount, offset);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/pwrite.2.html">pwrite(2)</a>.
     */
    public static int pwrite(java.io.FileDescriptor fd, java.nio.ByteBuffer buffer, long offset) throws android.system.ErrnoException, java.io.InterruptedIOException {
        return Libcore.os.pwrite(fd, buffer, offset);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/pwrite.2.html">pwrite(2)</a>.
     */
    public static int pwrite(java.io.FileDescriptor fd, byte[] bytes, int byteOffset, int byteCount, long offset) throws android.system.ErrnoException, java.io.InterruptedIOException {
        return Libcore.os.pwrite(fd, bytes, byteOffset, byteCount, offset);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/read.2.html">read(2)</a>.
     */
    public static int read(java.io.FileDescriptor fd, java.nio.ByteBuffer buffer) throws android.system.ErrnoException, java.io.InterruptedIOException {
        return Libcore.os.read(fd, buffer);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/read.2.html">read(2)</a>.
     */
    public static int read(java.io.FileDescriptor fd, byte[] bytes, int byteOffset, int byteCount) throws android.system.ErrnoException, java.io.InterruptedIOException {
        return Libcore.os.read(fd, bytes, byteOffset, byteCount);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/readlink.2.html">readlink(2)</a>.
     */
    public static java.lang.String readlink(java.lang.String path) throws android.system.ErrnoException {
        return Libcore.os.readlink(path);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/readv.2.html">readv(2)</a>.
     */
    public static int readv(java.io.FileDescriptor fd, java.lang.Object[] buffers, int[] offsets, int[] byteCounts) throws android.system.ErrnoException, java.io.InterruptedIOException {
        return Libcore.os.readv(fd, buffers, offsets, byteCounts);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/recvfrom.2.html">recvfrom(2)</a>.
     */
    public static int recvfrom(java.io.FileDescriptor fd, java.nio.ByteBuffer buffer, int flags, java.net.InetSocketAddress srcAddress) throws android.system.ErrnoException, java.net.SocketException {
        return Libcore.os.recvfrom(fd, buffer, flags, srcAddress);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/recvfrom.2.html">recvfrom(2)</a>.
     */
    public static int recvfrom(java.io.FileDescriptor fd, byte[] bytes, int byteOffset, int byteCount, int flags, java.net.InetSocketAddress srcAddress) throws android.system.ErrnoException, java.net.SocketException {
        return Libcore.os.recvfrom(fd, bytes, byteOffset, byteCount, flags, srcAddress);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man3/remove.3.html">remove(3)</a>.
     */
    public static void remove(java.lang.String path) throws android.system.ErrnoException {
        Libcore.os.remove(path);
    }

    /**
     *
     *
     * @unknown 
     */
    public static void removexattr(java.lang.String path, java.lang.String name) throws android.system.ErrnoException {
        Libcore.os.removexattr(path, name);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/rename.2.html">rename(2)</a>.
     */
    public static void rename(java.lang.String oldPath, java.lang.String newPath) throws android.system.ErrnoException {
        Libcore.os.rename(oldPath, newPath);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/sendfile.2.html">sendfile(2)</a>.
     */
    public static long sendfile(java.io.FileDescriptor outFd, java.io.FileDescriptor inFd, android.util.MutableLong inOffset, long byteCount) throws android.system.ErrnoException {
        return Libcore.os.sendfile(outFd, inFd, inOffset, byteCount);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/sendto.2.html">sendto(2)</a>.
     */
    public static int sendto(java.io.FileDescriptor fd, java.nio.ByteBuffer buffer, int flags, java.net.InetAddress inetAddress, int port) throws android.system.ErrnoException, java.net.SocketException {
        return Libcore.os.sendto(fd, buffer, flags, inetAddress, port);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/sendto.2.html">sendto(2)</a>.
     */
    public static int sendto(java.io.FileDescriptor fd, byte[] bytes, int byteOffset, int byteCount, int flags, java.net.InetAddress inetAddress, int port) throws android.system.ErrnoException, java.net.SocketException {
        return Libcore.os.sendto(fd, bytes, byteOffset, byteCount, flags, inetAddress, port);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/sendto.2.html">sendto(2)</a>.
     */
    /**
     *
     *
     * @unknown 
     */
    public static int sendto(java.io.FileDescriptor fd, byte[] bytes, int byteOffset, int byteCount, int flags, java.net.SocketAddress address) throws android.system.ErrnoException, java.net.SocketException {
        return Libcore.os.sendto(fd, bytes, byteOffset, byteCount, flags, address);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/setegid.2.html">setegid(2)</a>.
     */
    public static void setegid(int egid) throws android.system.ErrnoException {
        Libcore.os.setegid(egid);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man3/setenv.3.html">setenv(3)</a>.
     */
    public static void setenv(java.lang.String name, java.lang.String value, boolean overwrite) throws android.system.ErrnoException {
        Libcore.os.setenv(name, value, overwrite);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/seteuid.2.html">seteuid(2)</a>.
     */
    public static void seteuid(int euid) throws android.system.ErrnoException {
        Libcore.os.seteuid(euid);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/setgid.2.html">setgid(2)</a>.
     */
    public static void setgid(int gid) throws android.system.ErrnoException {
        Libcore.os.setgid(gid);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/setpgid.2.html">setpgid(2)</a>.
     */
    /**
     *
     *
     * @unknown 
     */
    public static void setpgid(int pid, int pgid) throws android.system.ErrnoException {
        Libcore.os.setpgid(pid, pgid);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/setregid.2.html">setregid(2)</a>.
     */
    /**
     *
     *
     * @unknown 
     */
    public static void setregid(int rgid, int egid) throws android.system.ErrnoException {
        Libcore.os.setregid(rgid, egid);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/setreuid.2.html">setreuid(2)</a>.
     */
    /**
     *
     *
     * @unknown 
     */
    public static void setreuid(int ruid, int euid) throws android.system.ErrnoException {
        Libcore.os.setreuid(ruid, euid);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/setsid.2.html">setsid(2)</a>.
     */
    public static int setsid() throws android.system.ErrnoException {
        return Libcore.os.setsid();
    }

    /**
     *
     *
     * @unknown 
     */
    public static void setsockoptByte(java.io.FileDescriptor fd, int level, int option, int value) throws android.system.ErrnoException {
        Libcore.os.setsockoptByte(fd, level, option, value);
    }

    /**
     *
     *
     * @unknown 
     */
    public static void setsockoptIfreq(java.io.FileDescriptor fd, int level, int option, java.lang.String value) throws android.system.ErrnoException {
        Libcore.os.setsockoptIfreq(fd, level, option, value);
    }

    /**
     *
     *
     * @unknown 
     */
    public static void setsockoptInt(java.io.FileDescriptor fd, int level, int option, int value) throws android.system.ErrnoException {
        Libcore.os.setsockoptInt(fd, level, option, value);
    }

    /**
     *
     *
     * @unknown 
     */
    public static void setsockoptIpMreqn(java.io.FileDescriptor fd, int level, int option, int value) throws android.system.ErrnoException {
        Libcore.os.setsockoptIpMreqn(fd, level, option, value);
    }

    /**
     *
     *
     * @unknown 
     */
    public static void setsockoptGroupReq(java.io.FileDescriptor fd, int level, int option, android.system.StructGroupReq value) throws android.system.ErrnoException {
        Libcore.os.setsockoptGroupReq(fd, level, option, value);
    }

    /**
     *
     *
     * @unknown 
     */
    public static void setsockoptGroupSourceReq(java.io.FileDescriptor fd, int level, int option, android.system.StructGroupSourceReq value) throws android.system.ErrnoException {
        Libcore.os.setsockoptGroupSourceReq(fd, level, option, value);
    }

    /**
     *
     *
     * @unknown 
     */
    public static void setsockoptLinger(java.io.FileDescriptor fd, int level, int option, android.system.StructLinger value) throws android.system.ErrnoException {
        Libcore.os.setsockoptLinger(fd, level, option, value);
    }

    /**
     *
     *
     * @unknown 
     */
    public static void setsockoptTimeval(java.io.FileDescriptor fd, int level, int option, android.system.StructTimeval value) throws android.system.ErrnoException {
        Libcore.os.setsockoptTimeval(fd, level, option, value);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/setuid.2.html">setuid(2)</a>.
     */
    public static void setuid(int uid) throws android.system.ErrnoException {
        Libcore.os.setuid(uid);
    }

    /**
     *
     *
     * @unknown 
     */
    public static void setxattr(java.lang.String path, java.lang.String name, byte[] value, int flags) throws android.system.ErrnoException {
        Libcore.os.setxattr(path, name, value, flags);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/shutdown.2.html">shutdown(2)</a>.
     */
    public static void shutdown(java.io.FileDescriptor fd, int how) throws android.system.ErrnoException {
        Libcore.os.shutdown(fd, how);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/socket.2.html">socket(2)</a>.
     */
    public static java.io.FileDescriptor socket(int domain, int type, int protocol) throws android.system.ErrnoException {
        return Libcore.os.socket(domain, type, protocol);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/socketpair.2.html">socketpair(2)</a>.
     */
    public static void socketpair(int domain, int type, int protocol, java.io.FileDescriptor fd1, java.io.FileDescriptor fd2) throws android.system.ErrnoException {
        Libcore.os.socketpair(domain, type, protocol, fd1, fd2);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/stat.2.html">stat(2)</a>.
     */
    public static android.system.StructStat stat(java.lang.String path) throws android.system.ErrnoException {
        return Libcore.os.stat(path);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/statvfs.2.html">statvfs(2)</a>.
     */
    public static android.system.StructStatVfs statvfs(java.lang.String path) throws android.system.ErrnoException {
        return Libcore.os.statvfs(path);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man3/strerror.3.html">strerror(2)</a>.
     */
    public static java.lang.String strerror(int errno) {
        return Libcore.os.strerror(errno);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man3/strsignal.3.html">strsignal(3)</a>.
     */
    public static java.lang.String strsignal(int signal) {
        return Libcore.os.strsignal(signal);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/symlink.2.html">symlink(2)</a>.
     */
    public static void symlink(java.lang.String oldPath, java.lang.String newPath) throws android.system.ErrnoException {
        Libcore.os.symlink(oldPath, newPath);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man3/sysconf.3.html">sysconf(3)</a>.
     */
    public static long sysconf(int name) {
        return Libcore.os.sysconf(name);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man3/tcdrain.3.html">tcdrain(3)</a>.
     */
    public static void tcdrain(java.io.FileDescriptor fd) throws android.system.ErrnoException {
        Libcore.os.tcdrain(fd);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man3/tcsendbreak.3.html">tcsendbreak(3)</a>.
     */
    public static void tcsendbreak(java.io.FileDescriptor fd, int duration) throws android.system.ErrnoException {
        Libcore.os.tcsendbreak(fd, duration);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/umask.2.html">umask(2)</a>.
     */
    public static int umask(int mask) {
        return Libcore.os.umask(mask);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/uname.2.html">uname(2)</a>.
     */
    public static android.system.StructUtsname uname() {
        return Libcore.os.uname();
    }

    /**
     *
     *
     * @unknown See <a href="http://man7.org/linux/man-pages/man2/unlink.2.html">unlink(2)</a>.
     */
    public static void unlink(java.lang.String pathname) throws android.system.ErrnoException {
        Libcore.os.unlink(pathname);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man3/unsetenv.3.html">unsetenv(3)</a>.
     */
    public static void unsetenv(java.lang.String name) throws android.system.ErrnoException {
        Libcore.os.unsetenv(name);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/waitpid.2.html">waitpid(2)</a>.
     */
    public static int waitpid(int pid, android.util.MutableInt status, int options) throws android.system.ErrnoException {
        return Libcore.os.waitpid(pid, status, options);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/write.2.html">write(2)</a>.
     */
    public static int write(java.io.FileDescriptor fd, java.nio.ByteBuffer buffer) throws android.system.ErrnoException, java.io.InterruptedIOException {
        return Libcore.os.write(fd, buffer);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/write.2.html">write(2)</a>.
     */
    public static int write(java.io.FileDescriptor fd, byte[] bytes, int byteOffset, int byteCount) throws android.system.ErrnoException, java.io.InterruptedIOException {
        return Libcore.os.write(fd, bytes, byteOffset, byteCount);
    }

    /**
     * See <a href="http://man7.org/linux/man-pages/man2/writev.2.html">writev(2)</a>.
     */
    public static int writev(java.io.FileDescriptor fd, java.lang.Object[] buffers, int[] offsets, int[] byteCounts) throws android.system.ErrnoException, java.io.InterruptedIOException {
        return Libcore.os.writev(fd, buffers, offsets, byteCounts);
    }
}

