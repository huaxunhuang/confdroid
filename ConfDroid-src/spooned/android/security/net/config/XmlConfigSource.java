package android.security.net.config;


/**
 * {@link ConfigSource} based on an XML configuration file.
 *
 * @unknown 
 */
public class XmlConfigSource implements android.security.net.config.ConfigSource {
    private static final int CONFIG_BASE = 0;

    private static final int CONFIG_DOMAIN = 1;

    private static final int CONFIG_DEBUG = 2;

    private final java.lang.Object mLock = new java.lang.Object();

    private final int mResourceId;

    private final boolean mDebugBuild;

    private final int mTargetSdkVersion;

    private boolean mInitialized;

    private android.security.net.config.NetworkSecurityConfig mDefaultConfig;

    private java.util.Set<android.util.Pair<android.security.net.config.Domain, android.security.net.config.NetworkSecurityConfig>> mDomainMap;

    private android.content.Context mContext;

    @com.android.internal.annotations.VisibleForTesting
    public XmlConfigSource(android.content.Context context, int resourceId) {
        this(context, resourceId, false);
    }

    @com.android.internal.annotations.VisibleForTesting
    public XmlConfigSource(android.content.Context context, int resourceId, boolean debugBuild) {
        this(context, resourceId, debugBuild, android.os.Build.VERSION_CODES.CUR_DEVELOPMENT);
    }

    public XmlConfigSource(android.content.Context context, int resourceId, boolean debugBuild, int targetSdkVersion) {
        mResourceId = resourceId;
        mContext = context;
        mDebugBuild = debugBuild;
        mTargetSdkVersion = targetSdkVersion;
    }

    public java.util.Set<android.util.Pair<android.security.net.config.Domain, android.security.net.config.NetworkSecurityConfig>> getPerDomainConfigs() {
        ensureInitialized();
        return mDomainMap;
    }

    public android.security.net.config.NetworkSecurityConfig getDefaultConfig() {
        ensureInitialized();
        return mDefaultConfig;
    }

    private static final java.lang.String getConfigString(int configType) {
        switch (configType) {
            case android.security.net.config.XmlConfigSource.CONFIG_BASE :
                return "base-config";
            case android.security.net.config.XmlConfigSource.CONFIG_DOMAIN :
                return "domain-config";
            case android.security.net.config.XmlConfigSource.CONFIG_DEBUG :
                return "debug-overrides";
            default :
                throw new java.lang.IllegalArgumentException("Unknown config type: " + configType);
        }
    }

    private void ensureInitialized() {
        synchronized(mLock) {
            if (mInitialized) {
                return;
            }
            try (android.content.res.XmlResourceParser parser = mContext.getResources().getXml(mResourceId)) {
                parseNetworkSecurityConfig(parser);
                mContext = null;
                mInitialized = true;
            } catch (android.content.res.Resources.NotFoundException | org.xmlpull.v1.XmlPullParserException | java.io.IOException | android.security.net.config.XmlConfigSource.ParserException e) {
                throw new java.lang.RuntimeException("Failed to parse XML configuration from " + mContext.getResources().getResourceEntryName(mResourceId), e);
            }
        }
    }

    private android.security.net.config.Pin parsePin(android.content.res.XmlResourceParser parser) throws android.security.net.config.XmlConfigSource.ParserException, java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        java.lang.String digestAlgorithm = parser.getAttributeValue(null, "digest");
        if (!android.security.net.config.Pin.isSupportedDigestAlgorithm(digestAlgorithm)) {
            throw new android.security.net.config.XmlConfigSource.ParserException(parser, "Unsupported pin digest algorithm: " + digestAlgorithm);
        }
        if (parser.next() != org.xmlpull.v1.XmlPullParser.TEXT) {
            throw new android.security.net.config.XmlConfigSource.ParserException(parser, "Missing pin digest");
        }
        java.lang.String digest = parser.getText().trim();
        byte[] decodedDigest = null;
        try {
            decodedDigest = android.util.Base64.decode(digest, 0);
        } catch (java.lang.IllegalArgumentException e) {
            throw new android.security.net.config.XmlConfigSource.ParserException(parser, "Invalid pin digest", e);
        }
        int expectedLength = android.security.net.config.Pin.getDigestLength(digestAlgorithm);
        if (decodedDigest.length != expectedLength) {
            throw new android.security.net.config.XmlConfigSource.ParserException(parser, (((("digest length " + decodedDigest.length) + " does not match expected length for ") + digestAlgorithm) + " of ") + expectedLength);
        }
        if (parser.next() != org.xmlpull.v1.XmlPullParser.END_TAG) {
            throw new android.security.net.config.XmlConfigSource.ParserException(parser, "pin contains additional elements");
        }
        return new android.security.net.config.Pin(digestAlgorithm, decodedDigest);
    }

    private android.security.net.config.PinSet parsePinSet(android.content.res.XmlResourceParser parser) throws android.security.net.config.XmlConfigSource.ParserException, java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        java.lang.String expirationDate = parser.getAttributeValue(null, "expiration");
        long expirationTimestampMilis = java.lang.Long.MAX_VALUE;
        if (expirationDate != null) {
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false);
                java.util.Date date = sdf.parse(expirationDate);
                if (date == null) {
                    throw new android.security.net.config.XmlConfigSource.ParserException(parser, "Invalid expiration date in pin-set");
                }
                expirationTimestampMilis = date.getTime();
            } catch (java.text.ParseException e) {
                throw new android.security.net.config.XmlConfigSource.ParserException(parser, "Invalid expiration date in pin-set", e);
            }
        }
        int outerDepth = parser.getDepth();
        java.util.Set<android.security.net.config.Pin> pins = new android.util.ArraySet<>();
        while (com.android.internal.util.XmlUtils.nextElementWithin(parser, outerDepth)) {
            java.lang.String tagName = parser.getName();
            if (tagName.equals("pin")) {
                pins.add(parsePin(parser));
            } else {
                com.android.internal.util.XmlUtils.skipCurrentTag(parser);
            }
        } 
        return new android.security.net.config.PinSet(pins, expirationTimestampMilis);
    }

    private android.security.net.config.Domain parseDomain(android.content.res.XmlResourceParser parser, java.util.Set<java.lang.String> seenDomains) throws android.security.net.config.XmlConfigSource.ParserException, java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        boolean includeSubdomains = parser.getAttributeBooleanValue(null, "includeSubdomains", false);
        if (parser.next() != org.xmlpull.v1.XmlPullParser.TEXT) {
            throw new android.security.net.config.XmlConfigSource.ParserException(parser, "Domain name missing");
        }
        java.lang.String domain = parser.getText().trim().toLowerCase(java.util.Locale.US);
        if (parser.next() != org.xmlpull.v1.XmlPullParser.END_TAG) {
            throw new android.security.net.config.XmlConfigSource.ParserException(parser, "domain contains additional elements");
        }
        // Domains are matched using a most specific match, so don't allow duplicates.
        // includeSubdomains isn't relevant here, both android.com + subdomains and android.com
        // match for android.com equally. Do not allow any duplicates period.
        if (!seenDomains.add(domain)) {
            throw new android.security.net.config.XmlConfigSource.ParserException(parser, domain + " has already been specified");
        }
        return new android.security.net.config.Domain(domain, includeSubdomains);
    }

    private android.security.net.config.CertificatesEntryRef parseCertificatesEntry(android.content.res.XmlResourceParser parser, boolean defaultOverridePins) throws android.security.net.config.XmlConfigSource.ParserException, java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        boolean overridePins = parser.getAttributeBooleanValue(null, "overridePins", defaultOverridePins);
        int sourceId = parser.getAttributeResourceValue(null, "src", -1);
        java.lang.String sourceString = parser.getAttributeValue(null, "src");
        android.security.net.config.CertificateSource source = null;
        if (sourceString == null) {
            throw new android.security.net.config.XmlConfigSource.ParserException(parser, "certificates element missing src attribute");
        }
        if (sourceId != (-1)) {
            // TODO: Cache ResourceCertificateSources by sourceId
            source = new android.security.net.config.ResourceCertificateSource(sourceId, mContext);
        } else
            if ("system".equals(sourceString)) {
                source = android.security.net.config.SystemCertificateSource.getInstance();
            } else
                if ("user".equals(sourceString)) {
                    source = android.security.net.config.UserCertificateSource.getInstance();
                } else {
                    throw new android.security.net.config.XmlConfigSource.ParserException(parser, "Unknown certificates src. " + "Should be one of system|user|@resourceVal");
                }


        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
        return new android.security.net.config.CertificatesEntryRef(source, overridePins);
    }

    private java.util.Collection<android.security.net.config.CertificatesEntryRef> parseTrustAnchors(android.content.res.XmlResourceParser parser, boolean defaultOverridePins) throws android.security.net.config.XmlConfigSource.ParserException, java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        int outerDepth = parser.getDepth();
        java.util.List<android.security.net.config.CertificatesEntryRef> anchors = new java.util.ArrayList<>();
        while (com.android.internal.util.XmlUtils.nextElementWithin(parser, outerDepth)) {
            java.lang.String tagName = parser.getName();
            if (tagName.equals("certificates")) {
                anchors.add(parseCertificatesEntry(parser, defaultOverridePins));
            } else {
                com.android.internal.util.XmlUtils.skipCurrentTag(parser);
            }
        } 
        return anchors;
    }

    private java.util.List<android.util.Pair<android.security.net.config.NetworkSecurityConfig.Builder, java.util.Set<android.security.net.config.Domain>>> parseConfigEntry(android.content.res.XmlResourceParser parser, java.util.Set<java.lang.String> seenDomains, android.security.net.config.NetworkSecurityConfig.Builder parentBuilder, int configType) throws android.security.net.config.XmlConfigSource.ParserException, java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        java.util.List<android.util.Pair<android.security.net.config.NetworkSecurityConfig.Builder, java.util.Set<android.security.net.config.Domain>>> builders = new java.util.ArrayList<>();
        android.security.net.config.NetworkSecurityConfig.Builder builder = new android.security.net.config.NetworkSecurityConfig.Builder();
        builder.setParent(parentBuilder);
        java.util.Set<android.security.net.config.Domain> domains = new android.util.ArraySet<>();
        boolean seenPinSet = false;
        boolean seenTrustAnchors = false;
        boolean defaultOverridePins = configType == android.security.net.config.XmlConfigSource.CONFIG_DEBUG;
        java.lang.String configName = parser.getName();
        int outerDepth = parser.getDepth();
        // Add this builder now so that this builder occurs before any of its children. This
        // makes the final build pass easier.
        builders.add(new android.util.Pair<>(builder, domains));
        // Parse config attributes. Only set values that are present, config inheritence will
        // handle the rest.
        for (int i = 0; i < parser.getAttributeCount(); i++) {
            java.lang.String name = parser.getAttributeName(i);
            if ("hstsEnforced".equals(name)) {
                builder.setHstsEnforced(parser.getAttributeBooleanValue(i, android.security.net.config.NetworkSecurityConfig.DEFAULT_HSTS_ENFORCED));
            } else
                if ("cleartextTrafficPermitted".equals(name)) {
                    builder.setCleartextTrafficPermitted(parser.getAttributeBooleanValue(i, android.security.net.config.NetworkSecurityConfig.DEFAULT_CLEARTEXT_TRAFFIC_PERMITTED));
                }

        }
        // Parse the config elements.
        while (com.android.internal.util.XmlUtils.nextElementWithin(parser, outerDepth)) {
            java.lang.String tagName = parser.getName();
            if ("domain".equals(tagName)) {
                if (configType != android.security.net.config.XmlConfigSource.CONFIG_DOMAIN) {
                    throw new android.security.net.config.XmlConfigSource.ParserException(parser, "domain element not allowed in " + android.security.net.config.XmlConfigSource.getConfigString(configType));
                }
                android.security.net.config.Domain domain = parseDomain(parser, seenDomains);
                domains.add(domain);
            } else
                if ("trust-anchors".equals(tagName)) {
                    if (seenTrustAnchors) {
                        throw new android.security.net.config.XmlConfigSource.ParserException(parser, "Multiple trust-anchor elements not allowed");
                    }
                    builder.addCertificatesEntryRefs(parseTrustAnchors(parser, defaultOverridePins));
                    seenTrustAnchors = true;
                } else
                    if ("pin-set".equals(tagName)) {
                        if (configType != android.security.net.config.XmlConfigSource.CONFIG_DOMAIN) {
                            throw new android.security.net.config.XmlConfigSource.ParserException(parser, "pin-set element not allowed in " + android.security.net.config.XmlConfigSource.getConfigString(configType));
                        }
                        if (seenPinSet) {
                            throw new android.security.net.config.XmlConfigSource.ParserException(parser, "Multiple pin-set elements not allowed");
                        }
                        builder.setPinSet(parsePinSet(parser));
                        seenPinSet = true;
                    } else
                        if ("domain-config".equals(tagName)) {
                            if (configType != android.security.net.config.XmlConfigSource.CONFIG_DOMAIN) {
                                throw new android.security.net.config.XmlConfigSource.ParserException(parser, "Nested domain-config not allowed in " + android.security.net.config.XmlConfigSource.getConfigString(configType));
                            }
                            builders.addAll(parseConfigEntry(parser, seenDomains, builder, configType));
                        } else {
                            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                        }



        } 
        if ((configType == android.security.net.config.XmlConfigSource.CONFIG_DOMAIN) && domains.isEmpty()) {
            throw new android.security.net.config.XmlConfigSource.ParserException(parser, "No domain elements in domain-config");
        }
        return builders;
    }

    private void addDebugAnchorsIfNeeded(android.security.net.config.NetworkSecurityConfig.Builder debugConfigBuilder, android.security.net.config.NetworkSecurityConfig.Builder builder) {
        if ((debugConfigBuilder == null) || (!debugConfigBuilder.hasCertificatesEntryRefs())) {
            return;
        }
        // Don't add trust anchors if not already present, the builder will inherit the anchors
        // from its parent, and that's where the trust anchors should be added.
        if (!builder.hasCertificatesEntryRefs()) {
            return;
        }
        builder.addCertificatesEntryRefs(debugConfigBuilder.getCertificatesEntryRefs());
    }

    private void parseNetworkSecurityConfig(android.content.res.XmlResourceParser parser) throws android.security.net.config.XmlConfigSource.ParserException, java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        java.util.Set<java.lang.String> seenDomains = new android.util.ArraySet<>();
        java.util.List<android.util.Pair<android.security.net.config.NetworkSecurityConfig.Builder, java.util.Set<android.security.net.config.Domain>>> builders = new java.util.ArrayList<>();
        android.security.net.config.NetworkSecurityConfig.Builder baseConfigBuilder = null;
        android.security.net.config.NetworkSecurityConfig.Builder debugConfigBuilder = null;
        boolean seenDebugOverrides = false;
        boolean seenBaseConfig = false;
        com.android.internal.util.XmlUtils.beginDocument(parser, "network-security-config");
        int outerDepth = parser.getDepth();
        while (com.android.internal.util.XmlUtils.nextElementWithin(parser, outerDepth)) {
            if ("base-config".equals(parser.getName())) {
                if (seenBaseConfig) {
                    throw new android.security.net.config.XmlConfigSource.ParserException(parser, "Only one base-config allowed");
                }
                seenBaseConfig = true;
                baseConfigBuilder = parseConfigEntry(parser, seenDomains, null, android.security.net.config.XmlConfigSource.CONFIG_BASE).get(0).first;
            } else
                if ("domain-config".equals(parser.getName())) {
                    builders.addAll(parseConfigEntry(parser, seenDomains, baseConfigBuilder, android.security.net.config.XmlConfigSource.CONFIG_DOMAIN));
                } else
                    if ("debug-overrides".equals(parser.getName())) {
                        if (seenDebugOverrides) {
                            throw new android.security.net.config.XmlConfigSource.ParserException(parser, "Only one debug-overrides allowed");
                        }
                        if (mDebugBuild) {
                            debugConfigBuilder = parseConfigEntry(parser, null, null, android.security.net.config.XmlConfigSource.CONFIG_DEBUG).get(0).first;
                        } else {
                            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                        }
                        seenDebugOverrides = true;
                    } else {
                        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                    }


        } 
        // If debug is true and there was no debug-overrides in the file check for an extra
        // _debug resource.
        if (mDebugBuild && (debugConfigBuilder == null)) {
            debugConfigBuilder = parseDebugOverridesResource();
        }
        // Use the platform default as the parent of the base config for any values not provided
        // there. If there is no base config use the platform default.
        android.security.net.config.NetworkSecurityConfig.Builder platformDefaultBuilder = android.security.net.config.NetworkSecurityConfig.getDefaultBuilder(mTargetSdkVersion);
        addDebugAnchorsIfNeeded(debugConfigBuilder, platformDefaultBuilder);
        if (baseConfigBuilder != null) {
            baseConfigBuilder.setParent(platformDefaultBuilder);
            addDebugAnchorsIfNeeded(debugConfigBuilder, baseConfigBuilder);
        } else {
            baseConfigBuilder = platformDefaultBuilder;
        }
        // Build the per-domain config mapping.
        java.util.Set<android.util.Pair<android.security.net.config.Domain, android.security.net.config.NetworkSecurityConfig>> configs = new android.util.ArraySet<>();
        for (android.util.Pair<android.security.net.config.NetworkSecurityConfig.Builder, java.util.Set<android.security.net.config.Domain>> entry : builders) {
            android.security.net.config.NetworkSecurityConfig.Builder builder = entry.first;
            java.util.Set<android.security.net.config.Domain> domains = entry.second;
            // Set the parent of configs that do not have a parent to the base-config. This can
            // happen if the base-config comes after a domain-config in the file.
            // Note that this is safe with regards to children because of the order that
            // parseConfigEntry returns builders, the parent is always before the children. The
            // children builders will not have build called until _after_ their parents have their
            // parent set so everything is consistent.
            if (builder.getParent() == null) {
                builder.setParent(baseConfigBuilder);
            }
            addDebugAnchorsIfNeeded(debugConfigBuilder, builder);
            android.security.net.config.NetworkSecurityConfig config = builder.build();
            for (android.security.net.config.Domain domain : domains) {
                configs.add(new android.util.Pair<>(domain, config));
            }
        }
        mDefaultConfig = baseConfigBuilder.build();
        mDomainMap = configs;
    }

    private android.security.net.config.NetworkSecurityConfig.Builder parseDebugOverridesResource() throws android.security.net.config.XmlConfigSource.ParserException, java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.res.Resources resources = mContext.getResources();
        java.lang.String packageName = resources.getResourcePackageName(mResourceId);
        java.lang.String entryName = resources.getResourceEntryName(mResourceId);
        int resId = resources.getIdentifier(entryName + "_debug", "xml", packageName);
        // No debug-overrides resource was found, nothing to parse.
        if (resId == 0) {
            return null;
        }
        android.security.net.config.NetworkSecurityConfig.Builder debugConfigBuilder = null;
        // Parse debug-overrides out of the _debug resource.
        try (android.content.res.XmlResourceParser parser = resources.getXml(resId)) {
            com.android.internal.util.XmlUtils.beginDocument(parser, "network-security-config");
            int outerDepth = parser.getDepth();
            boolean seenDebugOverrides = false;
            while (com.android.internal.util.XmlUtils.nextElementWithin(parser, outerDepth)) {
                if ("debug-overrides".equals(parser.getName())) {
                    if (seenDebugOverrides) {
                        throw new android.security.net.config.XmlConfigSource.ParserException(parser, "Only one debug-overrides allowed");
                    }
                    if (mDebugBuild) {
                        debugConfigBuilder = parseConfigEntry(parser, null, null, android.security.net.config.XmlConfigSource.CONFIG_DEBUG).get(0).first;
                    } else {
                        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                    }
                    seenDebugOverrides = true;
                } else {
                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                }
            } 
        }
        return debugConfigBuilder;
    }

    public static class ParserException extends java.lang.Exception {
        public ParserException(org.xmlpull.v1.XmlPullParser parser, java.lang.String message, java.lang.Throwable cause) {
            super((message + " at: ") + parser.getPositionDescription(), cause);
        }

        public ParserException(org.xmlpull.v1.XmlPullParser parser, java.lang.String message) {
            this(parser, message, null);
        }
    }
}

