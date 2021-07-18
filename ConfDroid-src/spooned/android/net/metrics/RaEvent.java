/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.net.metrics;


/**
 * An event logged when the APF packet socket receives an RA packet.
 * {@hide }
 */
@android.annotation.SystemApi
public final class RaEvent implements android.os.Parcelable {
    /**
     * {@hide }
     */
    public static final long NO_LIFETIME = -1L;

    // Lifetime in seconds of options found in a single RA packet.
    // When an option is not set, the value of the associated field is -1;
    public final long routerLifetime;

    public final long prefixValidLifetime;

    public final long prefixPreferredLifetime;

    public final long routeInfoLifetime;

    public final long rdnssLifetime;

    public final long dnsslLifetime;

    /**
     * {@hide }
     */
    public RaEvent(long routerLifetime, long prefixValidLifetime, long prefixPreferredLifetime, long routeInfoLifetime, long rdnssLifetime, long dnsslLifetime) {
        this.routerLifetime = routerLifetime;
        this.prefixValidLifetime = prefixValidLifetime;
        this.prefixPreferredLifetime = prefixPreferredLifetime;
        this.routeInfoLifetime = routeInfoLifetime;
        this.rdnssLifetime = rdnssLifetime;
        this.dnsslLifetime = dnsslLifetime;
    }

    private RaEvent(android.os.Parcel in) {
        routerLifetime = in.readLong();
        prefixValidLifetime = in.readLong();
        prefixPreferredLifetime = in.readLong();
        routeInfoLifetime = in.readLong();
        rdnssLifetime = in.readLong();
        dnsslLifetime = in.readLong();
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeLong(routerLifetime);
        out.writeLong(prefixValidLifetime);
        out.writeLong(prefixPreferredLifetime);
        out.writeLong(routeInfoLifetime);
        out.writeLong(rdnssLifetime);
        out.writeLong(dnsslLifetime);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return new java.lang.StringBuilder("RaEvent(lifetimes: ").append(java.lang.String.format("router=%ds, ", routerLifetime)).append(java.lang.String.format("prefix_valid=%ds, ", prefixValidLifetime)).append(java.lang.String.format("prefix_preferred=%ds, ", prefixPreferredLifetime)).append(java.lang.String.format("route_info=%ds, ", routeInfoLifetime)).append(java.lang.String.format("rdnss=%ds, ", rdnssLifetime)).append(java.lang.String.format("dnssl=%ds)", dnsslLifetime)).toString();
    }

    public static final android.os.Parcelable.Creator<android.net.metrics.RaEvent> CREATOR = new android.os.Parcelable.Creator<android.net.metrics.RaEvent>() {
        public android.net.metrics.RaEvent createFromParcel(android.os.Parcel in) {
            return new android.net.metrics.RaEvent(in);
        }

        public android.net.metrics.RaEvent[] newArray(int size) {
            return new android.net.metrics.RaEvent[size];
        }
    };

    /**
     * {@hide }
     */
    public static class Builder {
        long routerLifetime = android.net.metrics.RaEvent.NO_LIFETIME;

        long prefixValidLifetime = android.net.metrics.RaEvent.NO_LIFETIME;

        long prefixPreferredLifetime = android.net.metrics.RaEvent.NO_LIFETIME;

        long routeInfoLifetime = android.net.metrics.RaEvent.NO_LIFETIME;

        long rdnssLifetime = android.net.metrics.RaEvent.NO_LIFETIME;

        long dnsslLifetime = android.net.metrics.RaEvent.NO_LIFETIME;

        public Builder() {
        }

        public android.net.metrics.RaEvent build() {
            return new android.net.metrics.RaEvent(routerLifetime, prefixValidLifetime, prefixPreferredLifetime, routeInfoLifetime, rdnssLifetime, dnsslLifetime);
        }

        public android.net.metrics.RaEvent.Builder updateRouterLifetime(long lifetime) {
            routerLifetime = updateLifetime(routerLifetime, lifetime);
            return this;
        }

        public android.net.metrics.RaEvent.Builder updatePrefixValidLifetime(long lifetime) {
            prefixValidLifetime = updateLifetime(prefixValidLifetime, lifetime);
            return this;
        }

        public android.net.metrics.RaEvent.Builder updatePrefixPreferredLifetime(long lifetime) {
            prefixPreferredLifetime = updateLifetime(prefixPreferredLifetime, lifetime);
            return this;
        }

        public android.net.metrics.RaEvent.Builder updateRouteInfoLifetime(long lifetime) {
            routeInfoLifetime = updateLifetime(routeInfoLifetime, lifetime);
            return this;
        }

        public android.net.metrics.RaEvent.Builder updateRdnssLifetime(long lifetime) {
            rdnssLifetime = updateLifetime(rdnssLifetime, lifetime);
            return this;
        }

        public android.net.metrics.RaEvent.Builder updateDnsslLifetime(long lifetime) {
            dnsslLifetime = updateLifetime(dnsslLifetime, lifetime);
            return this;
        }

        private long updateLifetime(long currentLifetime, long newLifetime) {
            if (currentLifetime == android.net.metrics.RaEvent.NO_LIFETIME) {
                return newLifetime;
            }
            return java.lang.Math.min(currentLifetime, newLifetime);
        }
    }
}

