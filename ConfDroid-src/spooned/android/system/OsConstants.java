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
 * Constants and helper functions for use with {@link Os}.
 */
public final class OsConstants {
    private OsConstants() {
    }

    /**
     * Tests whether the given mode is a block device.
     */
    public static boolean S_ISBLK(int mode) {
        return (mode & android.system.OsConstants.S_IFMT) == android.system.OsConstants.S_IFBLK;
    }

    /**
     * Tests whether the given mode is a character device.
     */
    public static boolean S_ISCHR(int mode) {
        return (mode & android.system.OsConstants.S_IFMT) == android.system.OsConstants.S_IFCHR;
    }

    /**
     * Tests whether the given mode is a directory.
     */
    public static boolean S_ISDIR(int mode) {
        return (mode & android.system.OsConstants.S_IFMT) == android.system.OsConstants.S_IFDIR;
    }

    /**
     * Tests whether the given mode is a FIFO.
     */
    public static boolean S_ISFIFO(int mode) {
        return (mode & android.system.OsConstants.S_IFMT) == android.system.OsConstants.S_IFIFO;
    }

    /**
     * Tests whether the given mode is a regular file.
     */
    public static boolean S_ISREG(int mode) {
        return (mode & android.system.OsConstants.S_IFMT) == android.system.OsConstants.S_IFREG;
    }

    /**
     * Tests whether the given mode is a symbolic link.
     */
    public static boolean S_ISLNK(int mode) {
        return (mode & android.system.OsConstants.S_IFMT) == android.system.OsConstants.S_IFLNK;
    }

    /**
     * Tests whether the given mode is a socket.
     */
    public static boolean S_ISSOCK(int mode) {
        return (mode & android.system.OsConstants.S_IFMT) == android.system.OsConstants.S_IFSOCK;
    }

    /**
     * Extracts the exit status of a child. Only valid if WIFEXITED returns true.
     */
    public static int WEXITSTATUS(int status) {
        return (status & 0xff00) >> 8;
    }

    /**
     * Tests whether the child dumped core. Only valid if WIFSIGNALED returns true.
     */
    public static boolean WCOREDUMP(int status) {
        return (status & 0x80) != 0;
    }

    /**
     * Returns the signal that caused the child to exit. Only valid if WIFSIGNALED returns true.
     */
    public static int WTERMSIG(int status) {
        return status & 0x7f;
    }

    /**
     * Returns the signal that cause the child to stop. Only valid if WIFSTOPPED returns true.
     */
    public static int WSTOPSIG(int status) {
        return android.system.OsConstants.WEXITSTATUS(status);
    }

    /**
     * Tests whether the child exited normally.
     */
    public static boolean WIFEXITED(int status) {
        return android.system.OsConstants.WTERMSIG(status) == 0;
    }

    /**
     * Tests whether the child was stopped (not terminated) by a signal.
     */
    public static boolean WIFSTOPPED(int status) {
        return android.system.OsConstants.WTERMSIG(status) == 0x7f;
    }

    /**
     * Tests whether the child was terminated by a signal.
     */
    public static boolean WIFSIGNALED(int status) {
        return android.system.OsConstants.WTERMSIG(status + 1) >= 2;
    }

    public static final int AF_INET = android.system.OsConstants.placeholder();

    public static final int AF_INET6 = android.system.OsConstants.placeholder();

    /**
     *
     *
     * @unknown 
     */
    public static final int AF_NETLINK = android.system.OsConstants.placeholder();

    /**
     *
     *
     * @unknown 
     */
    public static final int AF_PACKET = android.system.OsConstants.placeholder();

    public static final int AF_UNIX = android.system.OsConstants.placeholder();

    public static final int AF_UNSPEC = android.system.OsConstants.placeholder();

    public static final int AI_ADDRCONFIG = android.system.OsConstants.placeholder();

    public static final int AI_ALL = android.system.OsConstants.placeholder();

    public static final int AI_CANONNAME = android.system.OsConstants.placeholder();

    public static final int AI_NUMERICHOST = android.system.OsConstants.placeholder();

    public static final int AI_NUMERICSERV = android.system.OsConstants.placeholder();

    public static final int AI_PASSIVE = android.system.OsConstants.placeholder();

    public static final int AI_V4MAPPED = android.system.OsConstants.placeholder();

    /**
     *
     *
     * @unknown 
     */
    public static final int ARPHRD_ETHER = android.system.OsConstants.placeholder();

    /**
     *
     *
     * @unknown 
     */
    public static final int ARPHRD_LOOPBACK = android.system.OsConstants.placeholder();

    public static final int CAP_AUDIT_CONTROL = android.system.OsConstants.placeholder();

    public static final int CAP_AUDIT_WRITE = android.system.OsConstants.placeholder();

    public static final int CAP_BLOCK_SUSPEND = android.system.OsConstants.placeholder();

    public static final int CAP_CHOWN = android.system.OsConstants.placeholder();

    public static final int CAP_DAC_OVERRIDE = android.system.OsConstants.placeholder();

    public static final int CAP_DAC_READ_SEARCH = android.system.OsConstants.placeholder();

    public static final int CAP_FOWNER = android.system.OsConstants.placeholder();

    public static final int CAP_FSETID = android.system.OsConstants.placeholder();

    public static final int CAP_IPC_LOCK = android.system.OsConstants.placeholder();

    public static final int CAP_IPC_OWNER = android.system.OsConstants.placeholder();

    public static final int CAP_KILL = android.system.OsConstants.placeholder();

    public static final int CAP_LAST_CAP = android.system.OsConstants.placeholder();

    public static final int CAP_LEASE = android.system.OsConstants.placeholder();

    public static final int CAP_LINUX_IMMUTABLE = android.system.OsConstants.placeholder();

    public static final int CAP_MAC_ADMIN = android.system.OsConstants.placeholder();

    public static final int CAP_MAC_OVERRIDE = android.system.OsConstants.placeholder();

    public static final int CAP_MKNOD = android.system.OsConstants.placeholder();

    public static final int CAP_NET_ADMIN = android.system.OsConstants.placeholder();

    public static final int CAP_NET_BIND_SERVICE = android.system.OsConstants.placeholder();

    public static final int CAP_NET_BROADCAST = android.system.OsConstants.placeholder();

    public static final int CAP_NET_RAW = android.system.OsConstants.placeholder();

    public static final int CAP_SETFCAP = android.system.OsConstants.placeholder();

    public static final int CAP_SETGID = android.system.OsConstants.placeholder();

    public static final int CAP_SETPCAP = android.system.OsConstants.placeholder();

    public static final int CAP_SETUID = android.system.OsConstants.placeholder();

    public static final int CAP_SYS_ADMIN = android.system.OsConstants.placeholder();

    public static final int CAP_SYS_BOOT = android.system.OsConstants.placeholder();

    public static final int CAP_SYS_CHROOT = android.system.OsConstants.placeholder();

    public static final int CAP_SYSLOG = android.system.OsConstants.placeholder();

    public static final int CAP_SYS_MODULE = android.system.OsConstants.placeholder();

    public static final int CAP_SYS_NICE = android.system.OsConstants.placeholder();

    public static final int CAP_SYS_PACCT = android.system.OsConstants.placeholder();

    public static final int CAP_SYS_PTRACE = android.system.OsConstants.placeholder();

    public static final int CAP_SYS_RAWIO = android.system.OsConstants.placeholder();

    public static final int CAP_SYS_RESOURCE = android.system.OsConstants.placeholder();

    public static final int CAP_SYS_TIME = android.system.OsConstants.placeholder();

    public static final int CAP_SYS_TTY_CONFIG = android.system.OsConstants.placeholder();

    public static final int CAP_WAKE_ALARM = android.system.OsConstants.placeholder();

    public static final int E2BIG = android.system.OsConstants.placeholder();

    public static final int EACCES = android.system.OsConstants.placeholder();

    public static final int EADDRINUSE = android.system.OsConstants.placeholder();

    public static final int EADDRNOTAVAIL = android.system.OsConstants.placeholder();

    public static final int EAFNOSUPPORT = android.system.OsConstants.placeholder();

    public static final int EAGAIN = android.system.OsConstants.placeholder();

    public static final int EAI_AGAIN = android.system.OsConstants.placeholder();

    public static final int EAI_BADFLAGS = android.system.OsConstants.placeholder();

    public static final int EAI_FAIL = android.system.OsConstants.placeholder();

    public static final int EAI_FAMILY = android.system.OsConstants.placeholder();

    public static final int EAI_MEMORY = android.system.OsConstants.placeholder();

    public static final int EAI_NODATA = android.system.OsConstants.placeholder();

    public static final int EAI_NONAME = android.system.OsConstants.placeholder();

    public static final int EAI_OVERFLOW = android.system.OsConstants.placeholder();

    public static final int EAI_SERVICE = android.system.OsConstants.placeholder();

    public static final int EAI_SOCKTYPE = android.system.OsConstants.placeholder();

    public static final int EAI_SYSTEM = android.system.OsConstants.placeholder();

    public static final int EALREADY = android.system.OsConstants.placeholder();

    public static final int EBADF = android.system.OsConstants.placeholder();

    public static final int EBADMSG = android.system.OsConstants.placeholder();

    public static final int EBUSY = android.system.OsConstants.placeholder();

    public static final int ECANCELED = android.system.OsConstants.placeholder();

    public static final int ECHILD = android.system.OsConstants.placeholder();

    public static final int ECONNABORTED = android.system.OsConstants.placeholder();

    public static final int ECONNREFUSED = android.system.OsConstants.placeholder();

    public static final int ECONNRESET = android.system.OsConstants.placeholder();

    public static final int EDEADLK = android.system.OsConstants.placeholder();

    public static final int EDESTADDRREQ = android.system.OsConstants.placeholder();

    public static final int EDOM = android.system.OsConstants.placeholder();

    public static final int EDQUOT = android.system.OsConstants.placeholder();

    public static final int EEXIST = android.system.OsConstants.placeholder();

    public static final int EFAULT = android.system.OsConstants.placeholder();

    public static final int EFBIG = android.system.OsConstants.placeholder();

    public static final int EHOSTUNREACH = android.system.OsConstants.placeholder();

    public static final int EIDRM = android.system.OsConstants.placeholder();

    public static final int EILSEQ = android.system.OsConstants.placeholder();

    public static final int EINPROGRESS = android.system.OsConstants.placeholder();

    public static final int EINTR = android.system.OsConstants.placeholder();

    public static final int EINVAL = android.system.OsConstants.placeholder();

    public static final int EIO = android.system.OsConstants.placeholder();

    public static final int EISCONN = android.system.OsConstants.placeholder();

    public static final int EISDIR = android.system.OsConstants.placeholder();

    public static final int ELOOP = android.system.OsConstants.placeholder();

    public static final int EMFILE = android.system.OsConstants.placeholder();

    public static final int EMLINK = android.system.OsConstants.placeholder();

    public static final int EMSGSIZE = android.system.OsConstants.placeholder();

    public static final int EMULTIHOP = android.system.OsConstants.placeholder();

    public static final int ENAMETOOLONG = android.system.OsConstants.placeholder();

    public static final int ENETDOWN = android.system.OsConstants.placeholder();

    public static final int ENETRESET = android.system.OsConstants.placeholder();

    public static final int ENETUNREACH = android.system.OsConstants.placeholder();

    public static final int ENFILE = android.system.OsConstants.placeholder();

    public static final int ENOBUFS = android.system.OsConstants.placeholder();

    public static final int ENODATA = android.system.OsConstants.placeholder();

    public static final int ENODEV = android.system.OsConstants.placeholder();

    public static final int ENOENT = android.system.OsConstants.placeholder();

    public static final int ENOEXEC = android.system.OsConstants.placeholder();

    public static final int ENOLCK = android.system.OsConstants.placeholder();

    public static final int ENOLINK = android.system.OsConstants.placeholder();

    public static final int ENOMEM = android.system.OsConstants.placeholder();

    public static final int ENOMSG = android.system.OsConstants.placeholder();

    /**
     *
     *
     * @unknown 
     */
    public static final int ENONET = android.system.OsConstants.placeholder();

    public static final int ENOPROTOOPT = android.system.OsConstants.placeholder();

    public static final int ENOSPC = android.system.OsConstants.placeholder();

    public static final int ENOSR = android.system.OsConstants.placeholder();

    public static final int ENOSTR = android.system.OsConstants.placeholder();

    public static final int ENOSYS = android.system.OsConstants.placeholder();

    public static final int ENOTCONN = android.system.OsConstants.placeholder();

    public static final int ENOTDIR = android.system.OsConstants.placeholder();

    public static final int ENOTEMPTY = android.system.OsConstants.placeholder();

    public static final int ENOTSOCK = android.system.OsConstants.placeholder();

    public static final int ENOTSUP = android.system.OsConstants.placeholder();

    public static final int ENOTTY = android.system.OsConstants.placeholder();

    public static final int ENXIO = android.system.OsConstants.placeholder();

    public static final int EOPNOTSUPP = android.system.OsConstants.placeholder();

    public static final int EOVERFLOW = android.system.OsConstants.placeholder();

    public static final int EPERM = android.system.OsConstants.placeholder();

    public static final int EPIPE = android.system.OsConstants.placeholder();

    public static final int EPROTO = android.system.OsConstants.placeholder();

    public static final int EPROTONOSUPPORT = android.system.OsConstants.placeholder();

    public static final int EPROTOTYPE = android.system.OsConstants.placeholder();

    public static final int ERANGE = android.system.OsConstants.placeholder();

    public static final int EROFS = android.system.OsConstants.placeholder();

    public static final int ESPIPE = android.system.OsConstants.placeholder();

    public static final int ESRCH = android.system.OsConstants.placeholder();

    public static final int ESTALE = android.system.OsConstants.placeholder();

    /**
     *
     *
     * @unknown 
     */
    public static final int ETH_P_ALL = android.system.OsConstants.placeholder();

    /**
     *
     *
     * @unknown 
     */
    public static final int ETH_P_ARP = android.system.OsConstants.placeholder();

    /**
     *
     *
     * @unknown 
     */
    public static final int ETH_P_IP = android.system.OsConstants.placeholder();

    /**
     *
     *
     * @unknown 
     */
    public static final int ETH_P_IPV6 = android.system.OsConstants.placeholder();

    public static final int ETIME = android.system.OsConstants.placeholder();

    public static final int ETIMEDOUT = android.system.OsConstants.placeholder();

    public static final int ETXTBSY = android.system.OsConstants.placeholder();

    /**
     *
     *
     * @unknown 
     */
    public static final int EUSERS = android.system.OsConstants.placeholder();

    // On Linux, EWOULDBLOCK == EAGAIN. Use EAGAIN instead, to reduce confusion.
    public static final int EXDEV = android.system.OsConstants.placeholder();

    public static final int EXIT_FAILURE = android.system.OsConstants.placeholder();

    public static final int EXIT_SUCCESS = android.system.OsConstants.placeholder();

    public static final int FD_CLOEXEC = android.system.OsConstants.placeholder();

    public static final int FIONREAD = android.system.OsConstants.placeholder();

    public static final int F_DUPFD = android.system.OsConstants.placeholder();

    /**
     *
     *
     * @unknown 
     */
    public static final int F_DUPFD_CLOEXEC = android.system.OsConstants.placeholder();

    public static final int F_GETFD = android.system.OsConstants.placeholder();

    public static final int F_GETFL = android.system.OsConstants.placeholder();

    public static final int F_GETLK = android.system.OsConstants.placeholder();

    public static final int F_GETLK64 = android.system.OsConstants.placeholder();

    public static final int F_GETOWN = android.system.OsConstants.placeholder();

    public static final int F_OK = android.system.OsConstants.placeholder();

    public static final int F_RDLCK = android.system.OsConstants.placeholder();

    public static final int F_SETFD = android.system.OsConstants.placeholder();

    public static final int F_SETFL = android.system.OsConstants.placeholder();

    public static final int F_SETLK = android.system.OsConstants.placeholder();

    public static final int F_SETLK64 = android.system.OsConstants.placeholder();

    public static final int F_SETLKW = android.system.OsConstants.placeholder();

    public static final int F_SETLKW64 = android.system.OsConstants.placeholder();

    public static final int F_SETOWN = android.system.OsConstants.placeholder();

    public static final int F_UNLCK = android.system.OsConstants.placeholder();

    public static final int F_WRLCK = android.system.OsConstants.placeholder();

    public static final int IFA_F_DADFAILED = android.system.OsConstants.placeholder();

    public static final int IFA_F_DEPRECATED = android.system.OsConstants.placeholder();

    public static final int IFA_F_HOMEADDRESS = android.system.OsConstants.placeholder();

    public static final int IFA_F_NODAD = android.system.OsConstants.placeholder();

    public static final int IFA_F_OPTIMISTIC = android.system.OsConstants.placeholder();

    public static final int IFA_F_PERMANENT = android.system.OsConstants.placeholder();

    public static final int IFA_F_SECONDARY = android.system.OsConstants.placeholder();

    public static final int IFA_F_TEMPORARY = android.system.OsConstants.placeholder();

    public static final int IFA_F_TENTATIVE = android.system.OsConstants.placeholder();

    public static final int IFF_ALLMULTI = android.system.OsConstants.placeholder();

    public static final int IFF_AUTOMEDIA = android.system.OsConstants.placeholder();

    public static final int IFF_BROADCAST = android.system.OsConstants.placeholder();

    public static final int IFF_DEBUG = android.system.OsConstants.placeholder();

    public static final int IFF_DYNAMIC = android.system.OsConstants.placeholder();

    public static final int IFF_LOOPBACK = android.system.OsConstants.placeholder();

    public static final int IFF_MASTER = android.system.OsConstants.placeholder();

    public static final int IFF_MULTICAST = android.system.OsConstants.placeholder();

    public static final int IFF_NOARP = android.system.OsConstants.placeholder();

    public static final int IFF_NOTRAILERS = android.system.OsConstants.placeholder();

    public static final int IFF_POINTOPOINT = android.system.OsConstants.placeholder();

    public static final int IFF_PORTSEL = android.system.OsConstants.placeholder();

    public static final int IFF_PROMISC = android.system.OsConstants.placeholder();

    public static final int IFF_RUNNING = android.system.OsConstants.placeholder();

    public static final int IFF_SLAVE = android.system.OsConstants.placeholder();

    public static final int IFF_UP = android.system.OsConstants.placeholder();

    public static final int IPPROTO_ICMP = android.system.OsConstants.placeholder();

    public static final int IPPROTO_ICMPV6 = android.system.OsConstants.placeholder();

    public static final int IPPROTO_IP = android.system.OsConstants.placeholder();

    public static final int IPPROTO_IPV6 = android.system.OsConstants.placeholder();

    public static final int IPPROTO_RAW = android.system.OsConstants.placeholder();

    public static final int IPPROTO_TCP = android.system.OsConstants.placeholder();

    public static final int IPPROTO_UDP = android.system.OsConstants.placeholder();

    public static final int IPV6_CHECKSUM = android.system.OsConstants.placeholder();

    public static final int IPV6_MULTICAST_HOPS = android.system.OsConstants.placeholder();

    public static final int IPV6_MULTICAST_IF = android.system.OsConstants.placeholder();

    public static final int IPV6_MULTICAST_LOOP = android.system.OsConstants.placeholder();

    public static final int IPV6_RECVDSTOPTS = android.system.OsConstants.placeholder();

    public static final int IPV6_RECVHOPLIMIT = android.system.OsConstants.placeholder();

    public static final int IPV6_RECVHOPOPTS = android.system.OsConstants.placeholder();

    public static final int IPV6_RECVPKTINFO = android.system.OsConstants.placeholder();

    public static final int IPV6_RECVRTHDR = android.system.OsConstants.placeholder();

    public static final int IPV6_RECVTCLASS = android.system.OsConstants.placeholder();

    public static final int IPV6_TCLASS = android.system.OsConstants.placeholder();

    public static final int IPV6_UNICAST_HOPS = android.system.OsConstants.placeholder();

    public static final int IPV6_V6ONLY = android.system.OsConstants.placeholder();

    public static final int IP_MULTICAST_IF = android.system.OsConstants.placeholder();

    public static final int IP_MULTICAST_LOOP = android.system.OsConstants.placeholder();

    public static final int IP_MULTICAST_TTL = android.system.OsConstants.placeholder();

    /**
     *
     *
     * @unknown 
     */
    public static final int IP_RECVTOS = android.system.OsConstants.placeholder();

    public static final int IP_TOS = android.system.OsConstants.placeholder();

    public static final int IP_TTL = android.system.OsConstants.placeholder();

    public static final int MAP_FIXED = android.system.OsConstants.placeholder();

    /**
     *
     *
     * @unknown 
     */
    public static final int MAP_POPULATE = android.system.OsConstants.placeholder();

    public static final int MAP_PRIVATE = android.system.OsConstants.placeholder();

    public static final int MAP_SHARED = android.system.OsConstants.placeholder();

    public static final int MCAST_JOIN_GROUP = android.system.OsConstants.placeholder();

    public static final int MCAST_LEAVE_GROUP = android.system.OsConstants.placeholder();

    public static final int MCAST_JOIN_SOURCE_GROUP = android.system.OsConstants.placeholder();

    public static final int MCAST_LEAVE_SOURCE_GROUP = android.system.OsConstants.placeholder();

    public static final int MCAST_BLOCK_SOURCE = android.system.OsConstants.placeholder();

    public static final int MCAST_UNBLOCK_SOURCE = android.system.OsConstants.placeholder();

    public static final int MCL_CURRENT = android.system.OsConstants.placeholder();

    public static final int MCL_FUTURE = android.system.OsConstants.placeholder();

    public static final int MSG_CTRUNC = android.system.OsConstants.placeholder();

    public static final int MSG_DONTROUTE = android.system.OsConstants.placeholder();

    public static final int MSG_EOR = android.system.OsConstants.placeholder();

    public static final int MSG_OOB = android.system.OsConstants.placeholder();

    public static final int MSG_PEEK = android.system.OsConstants.placeholder();

    public static final int MSG_TRUNC = android.system.OsConstants.placeholder();

    public static final int MSG_WAITALL = android.system.OsConstants.placeholder();

    public static final int MS_ASYNC = android.system.OsConstants.placeholder();

    public static final int MS_INVALIDATE = android.system.OsConstants.placeholder();

    public static final int MS_SYNC = android.system.OsConstants.placeholder();

    /**
     *
     *
     * @unknown 
     */
    public static final int NETLINK_ROUTE = android.system.OsConstants.placeholder();

    public static final int NI_DGRAM = android.system.OsConstants.placeholder();

    public static final int NI_NAMEREQD = android.system.OsConstants.placeholder();

    public static final int NI_NOFQDN = android.system.OsConstants.placeholder();

    public static final int NI_NUMERICHOST = android.system.OsConstants.placeholder();

    public static final int NI_NUMERICSERV = android.system.OsConstants.placeholder();

    public static final int O_ACCMODE = android.system.OsConstants.placeholder();

    public static final int O_APPEND = android.system.OsConstants.placeholder();

    /**
     *
     *
     * @unknown 
     */
    public static final int O_CLOEXEC = android.system.OsConstants.placeholder();

    public static final int O_CREAT = android.system.OsConstants.placeholder();

    public static final int O_EXCL = android.system.OsConstants.placeholder();

    public static final int O_NOCTTY = android.system.OsConstants.placeholder();

    public static final int O_NOFOLLOW = android.system.OsConstants.placeholder();

    public static final int O_NONBLOCK = android.system.OsConstants.placeholder();

    public static final int O_RDONLY = android.system.OsConstants.placeholder();

    public static final int O_RDWR = android.system.OsConstants.placeholder();

    public static final int O_SYNC = android.system.OsConstants.placeholder();

    /**
     *
     *
     * @unknown 
     */
    public static final int O_DSYNC = android.system.OsConstants.placeholder();

    public static final int O_TRUNC = android.system.OsConstants.placeholder();

    public static final int O_WRONLY = android.system.OsConstants.placeholder();

    public static final int POLLERR = android.system.OsConstants.placeholder();

    public static final int POLLHUP = android.system.OsConstants.placeholder();

    public static final int POLLIN = android.system.OsConstants.placeholder();

    public static final int POLLNVAL = android.system.OsConstants.placeholder();

    public static final int POLLOUT = android.system.OsConstants.placeholder();

    public static final int POLLPRI = android.system.OsConstants.placeholder();

    public static final int POLLRDBAND = android.system.OsConstants.placeholder();

    public static final int POLLRDNORM = android.system.OsConstants.placeholder();

    public static final int POLLWRBAND = android.system.OsConstants.placeholder();

    public static final int POLLWRNORM = android.system.OsConstants.placeholder();

    public static final int PR_GET_DUMPABLE = android.system.OsConstants.placeholder();

    public static final int PR_SET_DUMPABLE = android.system.OsConstants.placeholder();

    public static final int PR_SET_NO_NEW_PRIVS = android.system.OsConstants.placeholder();

    public static final int PROT_EXEC = android.system.OsConstants.placeholder();

    public static final int PROT_NONE = android.system.OsConstants.placeholder();

    public static final int PROT_READ = android.system.OsConstants.placeholder();

    public static final int PROT_WRITE = android.system.OsConstants.placeholder();

    public static final int R_OK = android.system.OsConstants.placeholder();

    public static final int RT_SCOPE_HOST = android.system.OsConstants.placeholder();

    public static final int RT_SCOPE_LINK = android.system.OsConstants.placeholder();

    public static final int RT_SCOPE_NOWHERE = android.system.OsConstants.placeholder();

    public static final int RT_SCOPE_SITE = android.system.OsConstants.placeholder();

    public static final int RT_SCOPE_UNIVERSE = android.system.OsConstants.placeholder();

    /**
     *
     *
     * @unknown 
     */
    public static final int RTMGRP_IPV4_IFADDR = android.system.OsConstants.placeholder();

    /**
     *
     *
     * @unknown 
     */
    public static final int RTMGRP_IPV4_MROUTE = android.system.OsConstants.placeholder();

    /**
     *
     *
     * @unknown 
     */
    public static final int RTMGRP_IPV4_ROUTE = android.system.OsConstants.placeholder();

    /**
     *
     *
     * @unknown 
     */
    public static final int RTMGRP_IPV4_RULE = android.system.OsConstants.placeholder();

    /**
     *
     *
     * @unknown 
     */
    public static final int RTMGRP_IPV6_IFADDR = android.system.OsConstants.placeholder();

    /**
     *
     *
     * @unknown 
     */
    public static final int RTMGRP_IPV6_IFINFO = android.system.OsConstants.placeholder();

    /**
     *
     *
     * @unknown 
     */
    public static final int RTMGRP_IPV6_MROUTE = android.system.OsConstants.placeholder();

    /**
     *
     *
     * @unknown 
     */
    public static final int RTMGRP_IPV6_PREFIX = android.system.OsConstants.placeholder();

    /**
     *
     *
     * @unknown 
     */
    public static final int RTMGRP_IPV6_ROUTE = android.system.OsConstants.placeholder();

    /**
     *
     *
     * @unknown 
     */
    public static final int RTMGRP_LINK = android.system.OsConstants.placeholder();

    /**
     *
     *
     * @unknown 
     */
    public static final int RTMGRP_NEIGH = android.system.OsConstants.placeholder();

    /**
     *
     *
     * @unknown 
     */
    public static final int RTMGRP_NOTIFY = android.system.OsConstants.placeholder();

    /**
     *
     *
     * @unknown 
     */
    public static final int RTMGRP_TC = android.system.OsConstants.placeholder();

    public static final int SEEK_CUR = android.system.OsConstants.placeholder();

    public static final int SEEK_END = android.system.OsConstants.placeholder();

    public static final int SEEK_SET = android.system.OsConstants.placeholder();

    public static final int SHUT_RD = android.system.OsConstants.placeholder();

    public static final int SHUT_RDWR = android.system.OsConstants.placeholder();

    public static final int SHUT_WR = android.system.OsConstants.placeholder();

    public static final int SIGABRT = android.system.OsConstants.placeholder();

    public static final int SIGALRM = android.system.OsConstants.placeholder();

    public static final int SIGBUS = android.system.OsConstants.placeholder();

    public static final int SIGCHLD = android.system.OsConstants.placeholder();

    public static final int SIGCONT = android.system.OsConstants.placeholder();

    public static final int SIGFPE = android.system.OsConstants.placeholder();

    public static final int SIGHUP = android.system.OsConstants.placeholder();

    public static final int SIGILL = android.system.OsConstants.placeholder();

    public static final int SIGINT = android.system.OsConstants.placeholder();

    public static final int SIGIO = android.system.OsConstants.placeholder();

    public static final int SIGKILL = android.system.OsConstants.placeholder();

    public static final int SIGPIPE = android.system.OsConstants.placeholder();

    public static final int SIGPROF = android.system.OsConstants.placeholder();

    public static final int SIGPWR = android.system.OsConstants.placeholder();

    public static final int SIGQUIT = android.system.OsConstants.placeholder();

    public static final int SIGRTMAX = android.system.OsConstants.placeholder();

    public static final int SIGRTMIN = android.system.OsConstants.placeholder();

    public static final int SIGSEGV = android.system.OsConstants.placeholder();

    public static final int SIGSTKFLT = android.system.OsConstants.placeholder();

    public static final int SIGSTOP = android.system.OsConstants.placeholder();

    public static final int SIGSYS = android.system.OsConstants.placeholder();

    public static final int SIGTERM = android.system.OsConstants.placeholder();

    public static final int SIGTRAP = android.system.OsConstants.placeholder();

    public static final int SIGTSTP = android.system.OsConstants.placeholder();

    public static final int SIGTTIN = android.system.OsConstants.placeholder();

    public static final int SIGTTOU = android.system.OsConstants.placeholder();

    public static final int SIGURG = android.system.OsConstants.placeholder();

    public static final int SIGUSR1 = android.system.OsConstants.placeholder();

    public static final int SIGUSR2 = android.system.OsConstants.placeholder();

    public static final int SIGVTALRM = android.system.OsConstants.placeholder();

    public static final int SIGWINCH = android.system.OsConstants.placeholder();

    public static final int SIGXCPU = android.system.OsConstants.placeholder();

    public static final int SIGXFSZ = android.system.OsConstants.placeholder();

    public static final int SIOCGIFADDR = android.system.OsConstants.placeholder();

    public static final int SIOCGIFBRDADDR = android.system.OsConstants.placeholder();

    public static final int SIOCGIFDSTADDR = android.system.OsConstants.placeholder();

    public static final int SIOCGIFNETMASK = android.system.OsConstants.placeholder();

    public static final int SOCK_DGRAM = android.system.OsConstants.placeholder();

    public static final int SOCK_RAW = android.system.OsConstants.placeholder();

    public static final int SOCK_SEQPACKET = android.system.OsConstants.placeholder();

    public static final int SOCK_STREAM = android.system.OsConstants.placeholder();

    public static final int SOL_SOCKET = android.system.OsConstants.placeholder();

    public static final int SO_BINDTODEVICE = android.system.OsConstants.placeholder();

    public static final int SO_BROADCAST = android.system.OsConstants.placeholder();

    public static final int SO_DEBUG = android.system.OsConstants.placeholder();

    public static final int SO_DONTROUTE = android.system.OsConstants.placeholder();

    public static final int SO_ERROR = android.system.OsConstants.placeholder();

    public static final int SO_KEEPALIVE = android.system.OsConstants.placeholder();

    public static final int SO_LINGER = android.system.OsConstants.placeholder();

    public static final int SO_OOBINLINE = android.system.OsConstants.placeholder();

    public static final int SO_PASSCRED = android.system.OsConstants.placeholder();

    public static final int SO_PEERCRED = android.system.OsConstants.placeholder();

    public static final int SO_RCVBUF = android.system.OsConstants.placeholder();

    public static final int SO_RCVLOWAT = android.system.OsConstants.placeholder();

    public static final int SO_RCVTIMEO = android.system.OsConstants.placeholder();

    public static final int SO_REUSEADDR = android.system.OsConstants.placeholder();

    public static final int SO_SNDBUF = android.system.OsConstants.placeholder();

    public static final int SO_SNDLOWAT = android.system.OsConstants.placeholder();

    public static final int SO_SNDTIMEO = android.system.OsConstants.placeholder();

    public static final int SO_TYPE = android.system.OsConstants.placeholder();

    public static final int STDERR_FILENO = android.system.OsConstants.placeholder();

    public static final int STDIN_FILENO = android.system.OsConstants.placeholder();

    public static final int STDOUT_FILENO = android.system.OsConstants.placeholder();

    public static final int ST_MANDLOCK = android.system.OsConstants.placeholder();

    public static final int ST_NOATIME = android.system.OsConstants.placeholder();

    public static final int ST_NODEV = android.system.OsConstants.placeholder();

    public static final int ST_NODIRATIME = android.system.OsConstants.placeholder();

    public static final int ST_NOEXEC = android.system.OsConstants.placeholder();

    public static final int ST_NOSUID = android.system.OsConstants.placeholder();

    public static final int ST_RDONLY = android.system.OsConstants.placeholder();

    public static final int ST_RELATIME = android.system.OsConstants.placeholder();

    public static final int ST_SYNCHRONOUS = android.system.OsConstants.placeholder();

    public static final int S_IFBLK = android.system.OsConstants.placeholder();

    public static final int S_IFCHR = android.system.OsConstants.placeholder();

    public static final int S_IFDIR = android.system.OsConstants.placeholder();

    public static final int S_IFIFO = android.system.OsConstants.placeholder();

    public static final int S_IFLNK = android.system.OsConstants.placeholder();

    public static final int S_IFMT = android.system.OsConstants.placeholder();

    public static final int S_IFREG = android.system.OsConstants.placeholder();

    public static final int S_IFSOCK = android.system.OsConstants.placeholder();

    public static final int S_IRGRP = android.system.OsConstants.placeholder();

    public static final int S_IROTH = android.system.OsConstants.placeholder();

    public static final int S_IRUSR = android.system.OsConstants.placeholder();

    public static final int S_IRWXG = android.system.OsConstants.placeholder();

    public static final int S_IRWXO = android.system.OsConstants.placeholder();

    public static final int S_IRWXU = android.system.OsConstants.placeholder();

    public static final int S_ISGID = android.system.OsConstants.placeholder();

    public static final int S_ISUID = android.system.OsConstants.placeholder();

    public static final int S_ISVTX = android.system.OsConstants.placeholder();

    public static final int S_IWGRP = android.system.OsConstants.placeholder();

    public static final int S_IWOTH = android.system.OsConstants.placeholder();

    public static final int S_IWUSR = android.system.OsConstants.placeholder();

    public static final int S_IXGRP = android.system.OsConstants.placeholder();

    public static final int S_IXOTH = android.system.OsConstants.placeholder();

    public static final int S_IXUSR = android.system.OsConstants.placeholder();

    public static final int TCP_NODELAY = android.system.OsConstants.placeholder();

    /**
     *
     *
     * @unknown 
     */
    public static final int TIOCOUTQ = android.system.OsConstants.placeholder();

    /**
     *
     *
     * @unknown 
     */
    public static final int UNIX_PATH_MAX = android.system.OsConstants.placeholder();

    public static final int WCONTINUED = android.system.OsConstants.placeholder();

    public static final int WEXITED = android.system.OsConstants.placeholder();

    public static final int WNOHANG = android.system.OsConstants.placeholder();

    public static final int WNOWAIT = android.system.OsConstants.placeholder();

    public static final int WSTOPPED = android.system.OsConstants.placeholder();

    public static final int WUNTRACED = android.system.OsConstants.placeholder();

    public static final int W_OK = android.system.OsConstants.placeholder();

    /**
     *
     *
     * @unknown 
     */
    public static final int XATTR_CREATE = android.system.OsConstants.placeholder();

    /**
     *
     *
     * @unknown 
     */
    public static final int XATTR_REPLACE = android.system.OsConstants.placeholder();

    public static final int X_OK = android.system.OsConstants.placeholder();

    public static final int _SC_2_CHAR_TERM = android.system.OsConstants.placeholder();

    public static final int _SC_2_C_BIND = android.system.OsConstants.placeholder();

    public static final int _SC_2_C_DEV = android.system.OsConstants.placeholder();

    public static final int _SC_2_C_VERSION = android.system.OsConstants.placeholder();

    public static final int _SC_2_FORT_DEV = android.system.OsConstants.placeholder();

    public static final int _SC_2_FORT_RUN = android.system.OsConstants.placeholder();

    public static final int _SC_2_LOCALEDEF = android.system.OsConstants.placeholder();

    public static final int _SC_2_SW_DEV = android.system.OsConstants.placeholder();

    public static final int _SC_2_UPE = android.system.OsConstants.placeholder();

    public static final int _SC_2_VERSION = android.system.OsConstants.placeholder();

    public static final int _SC_AIO_LISTIO_MAX = android.system.OsConstants.placeholder();

    public static final int _SC_AIO_MAX = android.system.OsConstants.placeholder();

    public static final int _SC_AIO_PRIO_DELTA_MAX = android.system.OsConstants.placeholder();

    public static final int _SC_ARG_MAX = android.system.OsConstants.placeholder();

    public static final int _SC_ASYNCHRONOUS_IO = android.system.OsConstants.placeholder();

    public static final int _SC_ATEXIT_MAX = android.system.OsConstants.placeholder();

    public static final int _SC_AVPHYS_PAGES = android.system.OsConstants.placeholder();

    public static final int _SC_BC_BASE_MAX = android.system.OsConstants.placeholder();

    public static final int _SC_BC_DIM_MAX = android.system.OsConstants.placeholder();

    public static final int _SC_BC_SCALE_MAX = android.system.OsConstants.placeholder();

    public static final int _SC_BC_STRING_MAX = android.system.OsConstants.placeholder();

    public static final int _SC_CHILD_MAX = android.system.OsConstants.placeholder();

    public static final int _SC_CLK_TCK = android.system.OsConstants.placeholder();

    public static final int _SC_COLL_WEIGHTS_MAX = android.system.OsConstants.placeholder();

    public static final int _SC_DELAYTIMER_MAX = android.system.OsConstants.placeholder();

    public static final int _SC_EXPR_NEST_MAX = android.system.OsConstants.placeholder();

    public static final int _SC_FSYNC = android.system.OsConstants.placeholder();

    public static final int _SC_GETGR_R_SIZE_MAX = android.system.OsConstants.placeholder();

    public static final int _SC_GETPW_R_SIZE_MAX = android.system.OsConstants.placeholder();

    public static final int _SC_IOV_MAX = android.system.OsConstants.placeholder();

    public static final int _SC_JOB_CONTROL = android.system.OsConstants.placeholder();

    public static final int _SC_LINE_MAX = android.system.OsConstants.placeholder();

    public static final int _SC_LOGIN_NAME_MAX = android.system.OsConstants.placeholder();

    public static final int _SC_MAPPED_FILES = android.system.OsConstants.placeholder();

    public static final int _SC_MEMLOCK = android.system.OsConstants.placeholder();

    public static final int _SC_MEMLOCK_RANGE = android.system.OsConstants.placeholder();

    public static final int _SC_MEMORY_PROTECTION = android.system.OsConstants.placeholder();

    public static final int _SC_MESSAGE_PASSING = android.system.OsConstants.placeholder();

    public static final int _SC_MQ_OPEN_MAX = android.system.OsConstants.placeholder();

    public static final int _SC_MQ_PRIO_MAX = android.system.OsConstants.placeholder();

    public static final int _SC_NGROUPS_MAX = android.system.OsConstants.placeholder();

    public static final int _SC_NPROCESSORS_CONF = android.system.OsConstants.placeholder();

    public static final int _SC_NPROCESSORS_ONLN = android.system.OsConstants.placeholder();

    public static final int _SC_OPEN_MAX = android.system.OsConstants.placeholder();

    public static final int _SC_PAGESIZE = android.system.OsConstants.placeholder();

    public static final int _SC_PAGE_SIZE = android.system.OsConstants.placeholder();

    public static final int _SC_PASS_MAX = android.system.OsConstants.placeholder();

    public static final int _SC_PHYS_PAGES = android.system.OsConstants.placeholder();

    public static final int _SC_PRIORITIZED_IO = android.system.OsConstants.placeholder();

    public static final int _SC_PRIORITY_SCHEDULING = android.system.OsConstants.placeholder();

    public static final int _SC_REALTIME_SIGNALS = android.system.OsConstants.placeholder();

    public static final int _SC_RE_DUP_MAX = android.system.OsConstants.placeholder();

    public static final int _SC_RTSIG_MAX = android.system.OsConstants.placeholder();

    public static final int _SC_SAVED_IDS = android.system.OsConstants.placeholder();

    public static final int _SC_SEMAPHORES = android.system.OsConstants.placeholder();

    public static final int _SC_SEM_NSEMS_MAX = android.system.OsConstants.placeholder();

    public static final int _SC_SEM_VALUE_MAX = android.system.OsConstants.placeholder();

    public static final int _SC_SHARED_MEMORY_OBJECTS = android.system.OsConstants.placeholder();

    public static final int _SC_SIGQUEUE_MAX = android.system.OsConstants.placeholder();

    public static final int _SC_STREAM_MAX = android.system.OsConstants.placeholder();

    public static final int _SC_SYNCHRONIZED_IO = android.system.OsConstants.placeholder();

    public static final int _SC_THREADS = android.system.OsConstants.placeholder();

    public static final int _SC_THREAD_ATTR_STACKADDR = android.system.OsConstants.placeholder();

    public static final int _SC_THREAD_ATTR_STACKSIZE = android.system.OsConstants.placeholder();

    public static final int _SC_THREAD_DESTRUCTOR_ITERATIONS = android.system.OsConstants.placeholder();

    public static final int _SC_THREAD_KEYS_MAX = android.system.OsConstants.placeholder();

    public static final int _SC_THREAD_PRIORITY_SCHEDULING = android.system.OsConstants.placeholder();

    public static final int _SC_THREAD_PRIO_INHERIT = android.system.OsConstants.placeholder();

    public static final int _SC_THREAD_PRIO_PROTECT = android.system.OsConstants.placeholder();

    public static final int _SC_THREAD_SAFE_FUNCTIONS = android.system.OsConstants.placeholder();

    public static final int _SC_THREAD_STACK_MIN = android.system.OsConstants.placeholder();

    public static final int _SC_THREAD_THREADS_MAX = android.system.OsConstants.placeholder();

    public static final int _SC_TIMERS = android.system.OsConstants.placeholder();

    public static final int _SC_TIMER_MAX = android.system.OsConstants.placeholder();

    public static final int _SC_TTY_NAME_MAX = android.system.OsConstants.placeholder();

    public static final int _SC_TZNAME_MAX = android.system.OsConstants.placeholder();

    public static final int _SC_VERSION = android.system.OsConstants.placeholder();

    public static final int _SC_XBS5_ILP32_OFF32 = android.system.OsConstants.placeholder();

    public static final int _SC_XBS5_ILP32_OFFBIG = android.system.OsConstants.placeholder();

    public static final int _SC_XBS5_LP64_OFF64 = android.system.OsConstants.placeholder();

    public static final int _SC_XBS5_LPBIG_OFFBIG = android.system.OsConstants.placeholder();

    public static final int _SC_XOPEN_CRYPT = android.system.OsConstants.placeholder();

    public static final int _SC_XOPEN_ENH_I18N = android.system.OsConstants.placeholder();

    public static final int _SC_XOPEN_LEGACY = android.system.OsConstants.placeholder();

    public static final int _SC_XOPEN_REALTIME = android.system.OsConstants.placeholder();

    public static final int _SC_XOPEN_REALTIME_THREADS = android.system.OsConstants.placeholder();

    public static final int _SC_XOPEN_SHM = android.system.OsConstants.placeholder();

    public static final int _SC_XOPEN_UNIX = android.system.OsConstants.placeholder();

    public static final int _SC_XOPEN_VERSION = android.system.OsConstants.placeholder();

    public static final int _SC_XOPEN_XCU_VERSION = android.system.OsConstants.placeholder();

    /**
     * Returns the string name of a getaddrinfo(3) error value.
     * For example, "EAI_AGAIN".
     */
    public static java.lang.String gaiName(int error) {
        if (error == android.system.OsConstants.EAI_AGAIN) {
            return "EAI_AGAIN";
        }
        if (error == android.system.OsConstants.EAI_BADFLAGS) {
            return "EAI_BADFLAGS";
        }
        if (error == android.system.OsConstants.EAI_FAIL) {
            return "EAI_FAIL";
        }
        if (error == android.system.OsConstants.EAI_FAMILY) {
            return "EAI_FAMILY";
        }
        if (error == android.system.OsConstants.EAI_MEMORY) {
            return "EAI_MEMORY";
        }
        if (error == android.system.OsConstants.EAI_NODATA) {
            return "EAI_NODATA";
        }
        if (error == android.system.OsConstants.EAI_NONAME) {
            return "EAI_NONAME";
        }
        if (error == android.system.OsConstants.EAI_OVERFLOW) {
            return "EAI_OVERFLOW";
        }
        if (error == android.system.OsConstants.EAI_SERVICE) {
            return "EAI_SERVICE";
        }
        if (error == android.system.OsConstants.EAI_SOCKTYPE) {
            return "EAI_SOCKTYPE";
        }
        if (error == android.system.OsConstants.EAI_SYSTEM) {
            return "EAI_SYSTEM";
        }
        return null;
    }

    /**
     * Returns the string name of an errno value.
     * For example, "EACCES". See {@link Os#strerror} for human-readable errno descriptions.
     */
    public static java.lang.String errnoName(int errno) {
        if (errno == android.system.OsConstants.E2BIG) {
            return "E2BIG";
        }
        if (errno == android.system.OsConstants.EACCES) {
            return "EACCES";
        }
        if (errno == android.system.OsConstants.EADDRINUSE) {
            return "EADDRINUSE";
        }
        if (errno == android.system.OsConstants.EADDRNOTAVAIL) {
            return "EADDRNOTAVAIL";
        }
        if (errno == android.system.OsConstants.EAFNOSUPPORT) {
            return "EAFNOSUPPORT";
        }
        if (errno == android.system.OsConstants.EAGAIN) {
            return "EAGAIN";
        }
        if (errno == android.system.OsConstants.EALREADY) {
            return "EALREADY";
        }
        if (errno == android.system.OsConstants.EBADF) {
            return "EBADF";
        }
        if (errno == android.system.OsConstants.EBADMSG) {
            return "EBADMSG";
        }
        if (errno == android.system.OsConstants.EBUSY) {
            return "EBUSY";
        }
        if (errno == android.system.OsConstants.ECANCELED) {
            return "ECANCELED";
        }
        if (errno == android.system.OsConstants.ECHILD) {
            return "ECHILD";
        }
        if (errno == android.system.OsConstants.ECONNABORTED) {
            return "ECONNABORTED";
        }
        if (errno == android.system.OsConstants.ECONNREFUSED) {
            return "ECONNREFUSED";
        }
        if (errno == android.system.OsConstants.ECONNRESET) {
            return "ECONNRESET";
        }
        if (errno == android.system.OsConstants.EDEADLK) {
            return "EDEADLK";
        }
        if (errno == android.system.OsConstants.EDESTADDRREQ) {
            return "EDESTADDRREQ";
        }
        if (errno == android.system.OsConstants.EDOM) {
            return "EDOM";
        }
        if (errno == android.system.OsConstants.EDQUOT) {
            return "EDQUOT";
        }
        if (errno == android.system.OsConstants.EEXIST) {
            return "EEXIST";
        }
        if (errno == android.system.OsConstants.EFAULT) {
            return "EFAULT";
        }
        if (errno == android.system.OsConstants.EFBIG) {
            return "EFBIG";
        }
        if (errno == android.system.OsConstants.EHOSTUNREACH) {
            return "EHOSTUNREACH";
        }
        if (errno == android.system.OsConstants.EIDRM) {
            return "EIDRM";
        }
        if (errno == android.system.OsConstants.EILSEQ) {
            return "EILSEQ";
        }
        if (errno == android.system.OsConstants.EINPROGRESS) {
            return "EINPROGRESS";
        }
        if (errno == android.system.OsConstants.EINTR) {
            return "EINTR";
        }
        if (errno == android.system.OsConstants.EINVAL) {
            return "EINVAL";
        }
        if (errno == android.system.OsConstants.EIO) {
            return "EIO";
        }
        if (errno == android.system.OsConstants.EISCONN) {
            return "EISCONN";
        }
        if (errno == android.system.OsConstants.EISDIR) {
            return "EISDIR";
        }
        if (errno == android.system.OsConstants.ELOOP) {
            return "ELOOP";
        }
        if (errno == android.system.OsConstants.EMFILE) {
            return "EMFILE";
        }
        if (errno == android.system.OsConstants.EMLINK) {
            return "EMLINK";
        }
        if (errno == android.system.OsConstants.EMSGSIZE) {
            return "EMSGSIZE";
        }
        if (errno == android.system.OsConstants.EMULTIHOP) {
            return "EMULTIHOP";
        }
        if (errno == android.system.OsConstants.ENAMETOOLONG) {
            return "ENAMETOOLONG";
        }
        if (errno == android.system.OsConstants.ENETDOWN) {
            return "ENETDOWN";
        }
        if (errno == android.system.OsConstants.ENETRESET) {
            return "ENETRESET";
        }
        if (errno == android.system.OsConstants.ENETUNREACH) {
            return "ENETUNREACH";
        }
        if (errno == android.system.OsConstants.ENFILE) {
            return "ENFILE";
        }
        if (errno == android.system.OsConstants.ENOBUFS) {
            return "ENOBUFS";
        }
        if (errno == android.system.OsConstants.ENODATA) {
            return "ENODATA";
        }
        if (errno == android.system.OsConstants.ENODEV) {
            return "ENODEV";
        }
        if (errno == android.system.OsConstants.ENOENT) {
            return "ENOENT";
        }
        if (errno == android.system.OsConstants.ENOEXEC) {
            return "ENOEXEC";
        }
        if (errno == android.system.OsConstants.ENOLCK) {
            return "ENOLCK";
        }
        if (errno == android.system.OsConstants.ENOLINK) {
            return "ENOLINK";
        }
        if (errno == android.system.OsConstants.ENOMEM) {
            return "ENOMEM";
        }
        if (errno == android.system.OsConstants.ENOMSG) {
            return "ENOMSG";
        }
        if (errno == android.system.OsConstants.ENONET) {
            return "ENONET";
        }
        if (errno == android.system.OsConstants.ENOPROTOOPT) {
            return "ENOPROTOOPT";
        }
        if (errno == android.system.OsConstants.ENOSPC) {
            return "ENOSPC";
        }
        if (errno == android.system.OsConstants.ENOSR) {
            return "ENOSR";
        }
        if (errno == android.system.OsConstants.ENOSTR) {
            return "ENOSTR";
        }
        if (errno == android.system.OsConstants.ENOSYS) {
            return "ENOSYS";
        }
        if (errno == android.system.OsConstants.ENOTCONN) {
            return "ENOTCONN";
        }
        if (errno == android.system.OsConstants.ENOTDIR) {
            return "ENOTDIR";
        }
        if (errno == android.system.OsConstants.ENOTEMPTY) {
            return "ENOTEMPTY";
        }
        if (errno == android.system.OsConstants.ENOTSOCK) {
            return "ENOTSOCK";
        }
        if (errno == android.system.OsConstants.ENOTSUP) {
            return "ENOTSUP";
        }
        if (errno == android.system.OsConstants.ENOTTY) {
            return "ENOTTY";
        }
        if (errno == android.system.OsConstants.ENXIO) {
            return "ENXIO";
        }
        if (errno == android.system.OsConstants.EOPNOTSUPP) {
            return "EOPNOTSUPP";
        }
        if (errno == android.system.OsConstants.EOVERFLOW) {
            return "EOVERFLOW";
        }
        if (errno == android.system.OsConstants.EPERM) {
            return "EPERM";
        }
        if (errno == android.system.OsConstants.EPIPE) {
            return "EPIPE";
        }
        if (errno == android.system.OsConstants.EPROTO) {
            return "EPROTO";
        }
        if (errno == android.system.OsConstants.EPROTONOSUPPORT) {
            return "EPROTONOSUPPORT";
        }
        if (errno == android.system.OsConstants.EPROTOTYPE) {
            return "EPROTOTYPE";
        }
        if (errno == android.system.OsConstants.ERANGE) {
            return "ERANGE";
        }
        if (errno == android.system.OsConstants.EROFS) {
            return "EROFS";
        }
        if (errno == android.system.OsConstants.ESPIPE) {
            return "ESPIPE";
        }
        if (errno == android.system.OsConstants.ESRCH) {
            return "ESRCH";
        }
        if (errno == android.system.OsConstants.ESTALE) {
            return "ESTALE";
        }
        if (errno == android.system.OsConstants.ETIME) {
            return "ETIME";
        }
        if (errno == android.system.OsConstants.ETIMEDOUT) {
            return "ETIMEDOUT";
        }
        if (errno == android.system.OsConstants.ETXTBSY) {
            return "ETXTBSY";
        }
        if (errno == android.system.OsConstants.EXDEV) {
            return "EXDEV";
        }
        return null;
    }

    private static native void initConstants();

    // A hack to avoid these constants being inlined by javac...
    private static int placeholder() {
        return 0;
    }

    // ...because we want to initialize them at runtime.
    static {
        android.system.OsConstants.initConstants();
    }
}

