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
package android.net;


/**
 * VpnService is a base class for applications to extend and build their
 * own VPN solutions. In general, it creates a virtual network interface,
 * configures addresses and routing rules, and returns a file descriptor
 * to the application. Each read from the descriptor retrieves an outgoing
 * packet which was routed to the interface. Each write to the descriptor
 * injects an incoming packet just like it was received from the interface.
 * The interface is running on Internet Protocol (IP), so packets are
 * always started with IP headers. The application then completes a VPN
 * connection by processing and exchanging packets with the remote server
 * over a tunnel.
 *
 * <p>Letting applications intercept packets raises huge security concerns.
 * A VPN application can easily break the network. Besides, two of them may
 * conflict with each other. The system takes several actions to address
 * these issues. Here are some key points:
 * <ul>
 *   <li>User action is required the first time an application creates a VPN
 *       connection.</li>
 *   <li>There can be only one VPN connection running at the same time. The
 *       existing interface is deactivated when a new one is created.</li>
 *   <li>A system-managed notification is shown during the lifetime of a
 *       VPN connection.</li>
 *   <li>A system-managed dialog gives the information of the current VPN
 *       connection. It also provides a button to disconnect.</li>
 *   <li>The network is restored automatically when the file descriptor is
 *       closed. It also covers the cases when a VPN application is crashed
 *       or killed by the system.</li>
 * </ul>
 *
 * <p>There are two primary methods in this class: {@link #prepare} and
 * {@link Builder#establish}. The former deals with user action and stops
 * the VPN connection created by another application. The latter creates
 * a VPN interface using the parameters supplied to the {@link Builder}.
 * An application must call {@link #prepare} to grant the right to use
 * other methods in this class, and the right can be revoked at any time.
 * Here are the general steps to create a VPN connection:
 * <ol>
 *   <li>When the user presses the button to connect, call {@link #prepare}
 *       and launch the returned intent, if non-null.</li>
 *   <li>When the application becomes prepared, start the service.</li>
 *   <li>Create a tunnel to the remote server and negotiate the network
 *       parameters for the VPN connection.</li>
 *   <li>Supply those parameters to a {@link Builder} and create a VPN
 *       interface by calling {@link Builder#establish}.</li>
 *   <li>Process and exchange packets between the tunnel and the returned
 *       file descriptor.</li>
 *   <li>When {@link #onRevoke} is invoked, close the file descriptor and
 *       shut down the tunnel gracefully.</li>
 * </ol>
 *
 * <p>Services extended this class need to be declared with appropriate
 * permission and intent filter. Their access must be secured by
 * {@link android.Manifest.permission#BIND_VPN_SERVICE} permission, and
 * their intent filter must match {@link #SERVICE_INTERFACE} action. Here
 * is an example of declaring a VPN service in {@code AndroidManifest.xml}:
 * <pre>
 * &lt;service android:name=".ExampleVpnService"
 *         android:permission="android.permission.BIND_VPN_SERVICE"&gt;
 *     &lt;intent-filter&gt;
 *         &lt;action android:name="android.net.VpnService"/&gt;
 *     &lt;/intent-filter&gt;
 * &lt;/service&gt;</pre>
 *
 * @see Builder
 */
public class VpnService extends android.app.Service {
    /**
     * The action must be matched by the intent filter of this service. It also
     * needs to require {@link android.Manifest.permission#BIND_VPN_SERVICE}
     * permission so that other applications cannot abuse it.
     */
    public static final java.lang.String SERVICE_INTERFACE = com.android.internal.net.VpnConfig.SERVICE_INTERFACE;

    /**
     * Use IConnectivityManager since those methods are hidden and not
     * available in ConnectivityManager.
     */
    private static android.net.IConnectivityManager getService() {
        return IConnectivityManager.Stub.asInterface(android.os.ServiceManager.getService(android.content.Context.CONNECTIVITY_SERVICE));
    }

    /**
     * Prepare to establish a VPN connection. This method returns {@code null}
     * if the VPN application is already prepared or if the user has previously
     * consented to the VPN application. Otherwise, it returns an
     * {@link Intent} to a system activity. The application should launch the
     * activity using {@link Activity#startActivityForResult} to get itself
     * prepared. The activity may pop up a dialog to require user action, and
     * the result will come back via its {@link Activity#onActivityResult}.
     * If the result is {@link Activity#RESULT_OK}, the application becomes
     * prepared and is granted to use other methods in this class.
     *
     * <p>Only one application can be granted at the same time. The right
     * is revoked when another application is granted. The application
     * losing the right will be notified via its {@link #onRevoke}. Unless
     * it becomes prepared again, subsequent calls to other methods in this
     * class will fail.
     *
     * <p>The user may disable the VPN at any time while it is activated, in
     * which case this method will return an intent the next time it is
     * executed to obtain the user's consent again.
     *
     * @see #onRevoke
     */
    public static android.content.Intent prepare(android.content.Context context) {
        try {
            if (android.net.VpnService.getService().prepareVpn(context.getPackageName(), null, android.os.UserHandle.myUserId())) {
                return null;
            }
        } catch (android.os.RemoteException e) {
            // ignore
        }
        return com.android.internal.net.VpnConfig.getIntentForConfirmation();
    }

    /**
     * Version of {@link #prepare(Context)} which does not require user consent.
     *
     * <p>Requires {@link android.Manifest.permission#CONTROL_VPN} and should generally not be
     * used. Only acceptable in situations where user consent has been obtained through other means.
     *
     * <p>Once this is run, future preparations may be done with the standard prepare method as this
     * will authorize the package to prepare the VPN without consent in the future.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public static void prepareAndAuthorize(android.content.Context context) {
        android.net.IConnectivityManager cm = android.net.VpnService.getService();
        java.lang.String packageName = context.getPackageName();
        try {
            // Only prepare if we're not already prepared.
            int userId = android.os.UserHandle.myUserId();
            if (!cm.prepareVpn(packageName, null, userId)) {
                cm.prepareVpn(null, packageName, userId);
            }
            cm.setVpnPackageAuthorization(packageName, userId, true);
        } catch (android.os.RemoteException e) {
            // ignore
        }
    }

    /**
     * Protect a socket from VPN connections. After protecting, data sent
     * through this socket will go directly to the underlying network,
     * so its traffic will not be forwarded through the VPN.
     * This method is useful if some connections need to be kept
     * outside of VPN. For example, a VPN tunnel should protect itself if its
     * destination is covered by VPN routes. Otherwise its outgoing packets
     * will be sent back to the VPN interface and cause an infinite loop. This
     * method will fail if the application is not prepared or is revoked.
     *
     * <p class="note">The socket is NOT closed by this method.
     *
     * @return {@code true} on success.
     */
    public boolean protect(int socket) {
        return android.net.NetworkUtils.protectFromVpn(socket);
    }

    /**
     * Convenience method to protect a {@link Socket} from VPN connections.
     *
     * @return {@code true} on success.
     * @see #protect(int)
     */
    public boolean protect(java.net.Socket socket) {
        return protect(socket.getFileDescriptor$().getInt$());
    }

    /**
     * Convenience method to protect a {@link DatagramSocket} from VPN
     * connections.
     *
     * @return {@code true} on success.
     * @see #protect(int)
     */
    public boolean protect(java.net.DatagramSocket socket) {
        return protect(socket.getFileDescriptor$().getInt$());
    }

    /**
     * Adds a network address to the VPN interface.
     *
     * Both IPv4 and IPv6 addresses are supported. The VPN must already be established. Fails if the
     * address is already in use or cannot be assigned to the interface for any other reason.
     *
     * Adding an address implicitly allows traffic from that address family (i.e., IPv4 or IPv6) to
     * be routed over the VPN. @see Builder#allowFamily
     *
     * @throws IllegalArgumentException
     * 		if the address is invalid.
     * @param address
     * 		The IP address (IPv4 or IPv6) to assign to the VPN interface.
     * @param prefixLength
     * 		The prefix length of the address.
     * @return {@code true} on success.
     * @see Builder#addAddress
     * @unknown 
     */
    public boolean addAddress(java.net.InetAddress address, int prefixLength) {
        android.net.VpnService.check(address, prefixLength);
        try {
            return android.net.VpnService.getService().addVpnAddress(address.getHostAddress(), prefixLength);
        } catch (android.os.RemoteException e) {
            throw new java.lang.IllegalStateException(e);
        }
    }

    /**
     * Removes a network address from the VPN interface.
     *
     * Both IPv4 and IPv6 addresses are supported. The VPN must already be established. Fails if the
     * address is not assigned to the VPN interface, or if it is the only address assigned (thus
     * cannot be removed), or if the address cannot be removed for any other reason.
     *
     * After removing an address, if there are no addresses, routes or DNS servers of a particular
     * address family (i.e., IPv4 or IPv6) configured on the VPN, that <b>DOES NOT</b> block that
     * family from being routed. In other words, once an address family has been allowed, it stays
     * allowed for the rest of the VPN's session. @see Builder#allowFamily
     *
     * @throws IllegalArgumentException
     * 		if the address is invalid.
     * @param address
     * 		The IP address (IPv4 or IPv6) to assign to the VPN interface.
     * @param prefixLength
     * 		The prefix length of the address.
     * @return {@code true} on success.
     * @unknown 
     */
    public boolean removeAddress(java.net.InetAddress address, int prefixLength) {
        android.net.VpnService.check(address, prefixLength);
        try {
            return android.net.VpnService.getService().removeVpnAddress(address.getHostAddress(), prefixLength);
        } catch (android.os.RemoteException e) {
            throw new java.lang.IllegalStateException(e);
        }
    }

    /**
     * Sets the underlying networks used by the VPN for its upstream connections.
     *
     * <p>Used by the system to know the actual networks that carry traffic for apps affected by
     * this VPN in order to present this information to the user (e.g., via status bar icons).
     *
     * <p>This method only needs to be called if the VPN has explicitly bound its underlying
     * communications channels &mdash; such as the socket(s) passed to {@link #protect(int)} &mdash;
     * to a {@code Network} using APIs such as {@link Network#bindSocket(Socket)} or
     * {@link Network#bindSocket(DatagramSocket)}. The VPN should call this method every time
     * the set of {@code Network}s it is using changes.
     *
     * <p>{@code networks} is one of the following:
     * <ul>
     * <li><strong>a non-empty array</strong>: an array of one or more {@link Network}s, in
     * decreasing preference order. For example, if this VPN uses both wifi and mobile (cellular)
     * networks to carry app traffic, but prefers or uses wifi more than mobile, wifi should appear
     * first in the array.</li>
     * <li><strong>an empty array</strong>: a zero-element array, meaning that the VPN has no
     * underlying network connection, and thus, app traffic will not be sent or received.</li>
     * <li><strong>null</strong>: (default) signifies that the VPN uses whatever is the system's
     * default network. I.e., it doesn't use the {@code bindSocket} or {@code bindDatagramSocket}
     * APIs mentioned above to send traffic over specific channels.</li>
     * </ul>
     *
     * <p>This call will succeed only if the VPN is currently established. For setting this value
     * when the VPN has not yet been established, see {@link Builder#setUnderlyingNetworks}.
     *
     * @param networks
     * 		An array of networks the VPN uses to tunnel traffic to/from its servers.
     * @return {@code true} on success.
     */
    public boolean setUnderlyingNetworks(android.net.Network[] networks) {
        try {
            return android.net.VpnService.getService().setUnderlyingNetworksForVpn(networks);
        } catch (android.os.RemoteException e) {
            throw new java.lang.IllegalStateException(e);
        }
    }

    /**
     * Return the communication interface to the service. This method returns
     * {@code null} on {@link Intent}s other than {@link #SERVICE_INTERFACE}
     * action. Applications overriding this method must identify the intent
     * and return the corresponding interface accordingly.
     *
     * @see Service#onBind
     */
    @java.lang.Override
    public android.os.IBinder onBind(android.content.Intent intent) {
        if ((intent != null) && android.net.VpnService.SERVICE_INTERFACE.equals(intent.getAction())) {
            return new android.net.VpnService.Callback();
        }
        return null;
    }

    /**
     * Invoked when the application is revoked. At this moment, the VPN
     * interface is already deactivated by the system. The application should
     * close the file descriptor and shut down gracefully. The default
     * implementation of this method is calling {@link Service#stopSelf()}.
     *
     * <p class="note">Calls to this method may not happen on the main thread
     * of the process.
     *
     * @see #prepare
     */
    public void onRevoke() {
        stopSelf();
    }

    /**
     * Use raw Binder instead of AIDL since now there is only one usage.
     */
    private class Callback extends android.os.Binder {
        @java.lang.Override
        protected boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) {
            if (code == android.os.IBinder.LAST_CALL_TRANSACTION) {
                onRevoke();
                return true;
            }
            return false;
        }
    }

    /**
     * Private method to validate address and prefixLength.
     */
    private static void check(java.net.InetAddress address, int prefixLength) {
        if (address.isLoopbackAddress()) {
            throw new java.lang.IllegalArgumentException("Bad address");
        }
        if (address instanceof java.net.Inet4Address) {
            if ((prefixLength < 0) || (prefixLength > 32)) {
                throw new java.lang.IllegalArgumentException("Bad prefixLength");
            }
        } else
            if (address instanceof java.net.Inet6Address) {
                if ((prefixLength < 0) || (prefixLength > 128)) {
                    throw new java.lang.IllegalArgumentException("Bad prefixLength");
                }
            } else {
                throw new java.lang.IllegalArgumentException("Unsupported family");
            }

    }

    /**
     * Helper class to create a VPN interface. This class should be always
     * used within the scope of the outer {@link VpnService}.
     *
     * @see VpnService
     */
    public class Builder {
        private final com.android.internal.net.VpnConfig mConfig = new com.android.internal.net.VpnConfig();

        private final java.util.List<android.net.LinkAddress> mAddresses = new java.util.ArrayList<android.net.LinkAddress>();

        private final java.util.List<android.net.RouteInfo> mRoutes = new java.util.ArrayList<android.net.RouteInfo>();

        public Builder() {
            mConfig.user = android.net.VpnService.this.getClass().getName();
        }

        /**
         * Set the name of this session. It will be displayed in
         * system-managed dialogs and notifications. This is recommended
         * not required.
         */
        public android.net.VpnService.Builder setSession(java.lang.String session) {
            mConfig.session = session;
            return this;
        }

        /**
         * Set the {@link PendingIntent} to an activity for users to
         * configure the VPN connection. If it is not set, the button
         * to configure will not be shown in system-managed dialogs.
         */
        public android.net.VpnService.Builder setConfigureIntent(android.app.PendingIntent intent) {
            mConfig.configureIntent = intent;
            return this;
        }

        /**
         * Set the maximum transmission unit (MTU) of the VPN interface. If
         * it is not set, the default value in the operating system will be
         * used.
         *
         * @throws IllegalArgumentException
         * 		if the value is not positive.
         */
        public android.net.VpnService.Builder setMtu(int mtu) {
            if (mtu <= 0) {
                throw new java.lang.IllegalArgumentException("Bad mtu");
            }
            mConfig.mtu = mtu;
            return this;
        }

        /**
         * Add a network address to the VPN interface. Both IPv4 and IPv6
         * addresses are supported. At least one address must be set before
         * calling {@link #establish}.
         *
         * Adding an address implicitly allows traffic from that address family
         * (i.e., IPv4 or IPv6) to be routed over the VPN. @see #allowFamily
         *
         * @throws IllegalArgumentException
         * 		if the address is invalid.
         */
        public android.net.VpnService.Builder addAddress(java.net.InetAddress address, int prefixLength) {
            android.net.VpnService.check(address, prefixLength);
            if (address.isAnyLocalAddress()) {
                throw new java.lang.IllegalArgumentException("Bad address");
            }
            mAddresses.add(new android.net.LinkAddress(address, prefixLength));
            mConfig.updateAllowedFamilies(address);
            return this;
        }

        /**
         * Convenience method to add a network address to the VPN interface
         * using a numeric address string. See {@link InetAddress} for the
         * definitions of numeric address formats.
         *
         * Adding an address implicitly allows traffic from that address family
         * (i.e., IPv4 or IPv6) to be routed over the VPN. @see #allowFamily
         *
         * @throws IllegalArgumentException
         * 		if the address is invalid.
         * @see #addAddress(InetAddress, int)
         */
        public android.net.VpnService.Builder addAddress(java.lang.String address, int prefixLength) {
            return addAddress(java.net.InetAddress.parseNumericAddress(address), prefixLength);
        }

        /**
         * Add a network route to the VPN interface. Both IPv4 and IPv6
         * routes are supported.
         *
         * Adding a route implicitly allows traffic from that address family
         * (i.e., IPv4 or IPv6) to be routed over the VPN. @see #allowFamily
         *
         * @throws IllegalArgumentException
         * 		if the route is invalid.
         */
        public android.net.VpnService.Builder addRoute(java.net.InetAddress address, int prefixLength) {
            android.net.VpnService.check(address, prefixLength);
            int offset = prefixLength / 8;
            byte[] bytes = address.getAddress();
            if (offset < bytes.length) {
                for (bytes[offset] <<= prefixLength % 8; offset < bytes.length; ++offset) {
                    if (bytes[offset] != 0) {
                        throw new java.lang.IllegalArgumentException("Bad address");
                    }
                }
            }
            mRoutes.add(new android.net.RouteInfo(new android.net.IpPrefix(address, prefixLength), null));
            mConfig.updateAllowedFamilies(address);
            return this;
        }

        /**
         * Convenience method to add a network route to the VPN interface
         * using a numeric address string. See {@link InetAddress} for the
         * definitions of numeric address formats.
         *
         * Adding a route implicitly allows traffic from that address family
         * (i.e., IPv4 or IPv6) to be routed over the VPN. @see #allowFamily
         *
         * @throws IllegalArgumentException
         * 		if the route is invalid.
         * @see #addRoute(InetAddress, int)
         */
        public android.net.VpnService.Builder addRoute(java.lang.String address, int prefixLength) {
            return addRoute(java.net.InetAddress.parseNumericAddress(address), prefixLength);
        }

        /**
         * Add a DNS server to the VPN connection. Both IPv4 and IPv6
         * addresses are supported. If none is set, the DNS servers of
         * the default network will be used.
         *
         * Adding a server implicitly allows traffic from that address family
         * (i.e., IPv4 or IPv6) to be routed over the VPN. @see #allowFamily
         *
         * @throws IllegalArgumentException
         * 		if the address is invalid.
         */
        public android.net.VpnService.Builder addDnsServer(java.net.InetAddress address) {
            if (address.isLoopbackAddress() || address.isAnyLocalAddress()) {
                throw new java.lang.IllegalArgumentException("Bad address");
            }
            if (mConfig.dnsServers == null) {
                mConfig.dnsServers = new java.util.ArrayList<java.lang.String>();
            }
            mConfig.dnsServers.add(address.getHostAddress());
            return this;
        }

        /**
         * Convenience method to add a DNS server to the VPN connection
         * using a numeric address string. See {@link InetAddress} for the
         * definitions of numeric address formats.
         *
         * Adding a server implicitly allows traffic from that address family
         * (i.e., IPv4 or IPv6) to be routed over the VPN. @see #allowFamily
         *
         * @throws IllegalArgumentException
         * 		if the address is invalid.
         * @see #addDnsServer(InetAddress)
         */
        public android.net.VpnService.Builder addDnsServer(java.lang.String address) {
            return addDnsServer(java.net.InetAddress.parseNumericAddress(address));
        }

        /**
         * Add a search domain to the DNS resolver.
         */
        public android.net.VpnService.Builder addSearchDomain(java.lang.String domain) {
            if (mConfig.searchDomains == null) {
                mConfig.searchDomains = new java.util.ArrayList<java.lang.String>();
            }
            mConfig.searchDomains.add(domain);
            return this;
        }

        /**
         * Allows traffic from the specified address family.
         *
         * By default, if no address, route or DNS server of a specific family (IPv4 or IPv6) is
         * added to this VPN, then all outgoing traffic of that family is blocked. If any address,
         * route or DNS server is added, that family is allowed.
         *
         * This method allows an address family to be unblocked even without adding an address,
         * route or DNS server of that family. Traffic of that family will then typically
         * fall-through to the underlying network if it's supported.
         *
         * {@code family} must be either {@code AF_INET} (for IPv4) or {@code AF_INET6} (for IPv6).
         * {@link IllegalArgumentException} is thrown if it's neither.
         *
         * @param family
         * 		The address family ({@code AF_INET} or {@code AF_INET6}) to allow.
         * @return this {@link Builder} object to facilitate chaining of method calls.
         */
        public android.net.VpnService.Builder allowFamily(int family) {
            if (family == android.system.OsConstants.AF_INET) {
                mConfig.allowIPv4 = true;
            } else
                if (family == android.system.OsConstants.AF_INET6) {
                    mConfig.allowIPv6 = true;
                } else {
                    throw new java.lang.IllegalArgumentException((((family + " is neither ") + android.system.OsConstants.AF_INET) + " nor ") + android.system.OsConstants.AF_INET6);
                }

            return this;
        }

        private void verifyApp(java.lang.String packageName) throws android.content.pm.PackageManager.NameNotFoundException {
            android.content.pm.IPackageManager pm = IPackageManager.Stub.asInterface(android.os.ServiceManager.getService("package"));
            try {
                pm.getApplicationInfo(packageName, 0, android.os.UserHandle.getCallingUserId());
            } catch (android.os.RemoteException e) {
                throw new java.lang.IllegalStateException(e);
            }
        }

        /**
         * Adds an application that's allowed to access the VPN connection.
         *
         * If this method is called at least once, only applications added through this method (and
         * no others) are allowed access. Else (if this method is never called), all applications
         * are allowed by default.  If some applications are added, other, un-added applications
         * will use networking as if the VPN wasn't running.
         *
         * A {@link Builder} may have only a set of allowed applications OR a set of disallowed
         * ones, but not both. Calling this method after {@link #addDisallowedApplication} has
         * already been called, or vice versa, will throw an {@link UnsupportedOperationException}.
         *
         * {@code packageName} must be the canonical name of a currently installed application.
         * {@link PackageManager.NameNotFoundException} is thrown if there's no such application.
         *
         * @throws PackageManager.NameNotFoundException
         * 		If the application isn't installed.
         * @param packageName
         * 		The full name (e.g.: "com.google.apps.contacts") of an application.
         * @return this {@link Builder} object to facilitate chaining method calls.
         */
        public android.net.VpnService.Builder addAllowedApplication(java.lang.String packageName) throws android.content.pm.PackageManager.NameNotFoundException {
            if (mConfig.disallowedApplications != null) {
                throw new java.lang.UnsupportedOperationException("addDisallowedApplication already called");
            }
            verifyApp(packageName);
            if (mConfig.allowedApplications == null) {
                mConfig.allowedApplications = new java.util.ArrayList<java.lang.String>();
            }
            mConfig.allowedApplications.add(packageName);
            return this;
        }

        /**
         * Adds an application that's denied access to the VPN connection.
         *
         * By default, all applications are allowed access, except for those denied through this
         * method.  Denied applications will use networking as if the VPN wasn't running.
         *
         * A {@link Builder} may have only a set of allowed applications OR a set of disallowed
         * ones, but not both. Calling this method after {@link #addAllowedApplication} has already
         * been called, or vice versa, will throw an {@link UnsupportedOperationException}.
         *
         * {@code packageName} must be the canonical name of a currently installed application.
         * {@link PackageManager.NameNotFoundException} is thrown if there's no such application.
         *
         * @throws PackageManager.NameNotFoundException
         * 		If the application isn't installed.
         * @param packageName
         * 		The full name (e.g.: "com.google.apps.contacts") of an application.
         * @return this {@link Builder} object to facilitate chaining method calls.
         */
        public android.net.VpnService.Builder addDisallowedApplication(java.lang.String packageName) throws android.content.pm.PackageManager.NameNotFoundException {
            if (mConfig.allowedApplications != null) {
                throw new java.lang.UnsupportedOperationException("addAllowedApplication already called");
            }
            verifyApp(packageName);
            if (mConfig.disallowedApplications == null) {
                mConfig.disallowedApplications = new java.util.ArrayList<java.lang.String>();
            }
            mConfig.disallowedApplications.add(packageName);
            return this;
        }

        /**
         * Allows all apps to bypass this VPN connection.
         *
         * By default, all traffic from apps is forwarded through the VPN interface and it is not
         * possible for apps to side-step the VPN. If this method is called, apps may use methods
         * such as {@link ConnectivityManager#bindProcessToNetwork} to instead send/receive
         * directly over the underlying network or any other network they have permissions for.
         *
         * @return this {@link Builder} object to facilitate chaining of method calls.
         */
        public android.net.VpnService.Builder allowBypass() {
            mConfig.allowBypass = true;
            return this;
        }

        /**
         * Sets the VPN interface's file descriptor to be in blocking/non-blocking mode.
         *
         * By default, the file descriptor returned by {@link #establish} is non-blocking.
         *
         * @param blocking
         * 		True to put the descriptor into blocking mode; false for non-blocking.
         * @return this {@link Builder} object to facilitate chaining method calls.
         */
        public android.net.VpnService.Builder setBlocking(boolean blocking) {
            mConfig.blocking = blocking;
            return this;
        }

        /**
         * Sets the underlying networks used by the VPN for its upstream connections.
         *
         * @see VpnService#setUnderlyingNetworks
         * @param networks
         * 		An array of networks the VPN uses to tunnel traffic to/from its servers.
         * @return this {@link Builder} object to facilitate chaining method calls.
         */
        public android.net.VpnService.Builder setUnderlyingNetworks(android.net.Network[] networks) {
            mConfig.underlyingNetworks = (networks != null) ? networks.clone() : null;
            return this;
        }

        /**
         * Create a VPN interface using the parameters supplied to this
         * builder. The interface works on IP packets, and a file descriptor
         * is returned for the application to access them. Each read
         * retrieves an outgoing packet which was routed to the interface.
         * Each write injects an incoming packet just like it was received
         * from the interface. The file descriptor is put into non-blocking
         * mode by default to avoid blocking Java threads. To use the file
         * descriptor completely in native space, see
         * {@link ParcelFileDescriptor#detachFd()}. The application MUST
         * close the file descriptor when the VPN connection is terminated.
         * The VPN interface will be removed and the network will be
         * restored by the system automatically.
         *
         * <p>To avoid conflicts, there can be only one active VPN interface
         * at the same time. Usually network parameters are never changed
         * during the lifetime of a VPN connection. It is also common for an
         * application to create a new file descriptor after closing the
         * previous one. However, it is rare but not impossible to have two
         * interfaces while performing a seamless handover. In this case, the
         * old interface will be deactivated when the new one is created
         * successfully. Both file descriptors are valid but now outgoing
         * packets will be routed to the new interface. Therefore, after
         * draining the old file descriptor, the application MUST close it
         * and start using the new file descriptor. If the new interface
         * cannot be created, the existing interface and its file descriptor
         * remain untouched.
         *
         * <p>An exception will be thrown if the interface cannot be created
         * for any reason. However, this method returns {@code null} if the
         * application is not prepared or is revoked. This helps solve
         * possible race conditions between other VPN applications.
         *
         * @return {@link ParcelFileDescriptor} of the VPN interface, or
        {@code null} if the application is not prepared.
         * @throws IllegalArgumentException
         * 		if a parameter is not accepted
         * 		by the operating system.
         * @throws IllegalStateException
         * 		if a parameter cannot be applied
         * 		by the operating system.
         * @throws SecurityException
         * 		if the service is not properly declared
         * 		in {@code AndroidManifest.xml}.
         * @see VpnService
         */
        public android.os.ParcelFileDescriptor establish() {
            mConfig.addresses = mAddresses;
            mConfig.routes = mRoutes;
            try {
                return android.net.VpnService.getService().establishVpn(mConfig);
            } catch (android.os.RemoteException e) {
                throw new java.lang.IllegalStateException(e);
            }
        }
    }
}

