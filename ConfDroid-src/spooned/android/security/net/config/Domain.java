/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.security.net.config;


/**
 *
 *
 * @unknown 
 */
public final class Domain {
    /**
     * Lower case hostname for this domain rule.
     */
    public final java.lang.String hostname;

    /**
     * Whether this domain includes subdomains.
     */
    public final boolean subdomainsIncluded;

    public Domain(java.lang.String hostname, boolean subdomainsIncluded) {
        if (hostname == null) {
            throw new java.lang.NullPointerException("Hostname must not be null");
        }
        this.hostname = hostname.toLowerCase(java.util.Locale.US);
        this.subdomainsIncluded = subdomainsIncluded;
    }

    @java.lang.Override
    public int hashCode() {
        return hostname.hashCode() ^ (subdomainsIncluded ? 1231 : 1237);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof android.security.net.config.Domain)) {
            return false;
        }
        android.security.net.config.Domain otherDomain = ((android.security.net.config.Domain) (other));
        return (otherDomain.subdomainsIncluded == this.subdomainsIncluded) && otherDomain.hostname.equals(this.hostname);
    }
}

