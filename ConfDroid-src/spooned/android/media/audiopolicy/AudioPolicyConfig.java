/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.media.audiopolicy;


/**
 *
 *
 * @unknown Internal storage class for AudioPolicy configuration.
 */
public class AudioPolicyConfig implements android.os.Parcelable {
    private static final java.lang.String TAG = "AudioPolicyConfig";

    protected java.util.ArrayList<android.media.audiopolicy.AudioMix> mMixes;

    protected int mDuckingPolicy = android.media.audiopolicy.AudioPolicy.FOCUS_POLICY_DUCKING_IN_APP;

    private java.lang.String mRegistrationId = null;

    protected AudioPolicyConfig(android.media.audiopolicy.AudioPolicyConfig conf) {
        mMixes = conf.mMixes;
    }

    AudioPolicyConfig(java.util.ArrayList<android.media.audiopolicy.AudioMix> mixes) {
        mMixes = mixes;
    }

    /**
     * Add an {@link AudioMix} to be part of the audio policy being built.
     *
     * @param mix
     * 		a non-null {@link AudioMix} to be part of the audio policy.
     * @return the same Builder instance.
     * @throws IllegalArgumentException
     * 		
     */
    public void addMix(android.media.audiopolicy.AudioMix mix) throws java.lang.IllegalArgumentException {
        if (mix == null) {
            throw new java.lang.IllegalArgumentException("Illegal null AudioMix argument");
        }
        mMixes.add(mix);
    }

    public java.util.ArrayList<android.media.audiopolicy.AudioMix> getMixes() {
        return mMixes;
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(mMixes);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mMixes.size());
        for (android.media.audiopolicy.AudioMix mix : mMixes) {
            // write mix route flags
            dest.writeInt(mix.getRouteFlags());
            // write callback flags
            dest.writeInt(mix.mCallbackFlags);
            // write device information
            dest.writeInt(mix.mDeviceSystemType);
            dest.writeString(mix.mDeviceAddress);
            // write mix format
            dest.writeInt(mix.getFormat().getSampleRate());
            dest.writeInt(mix.getFormat().getEncoding());
            dest.writeInt(mix.getFormat().getChannelMask());
            // write mix rules
            final java.util.ArrayList<android.media.audiopolicy.AudioMixingRule.AudioMixMatchCriterion> criteria = mix.getRule().getCriteria();
            dest.writeInt(criteria.size());
            for (android.media.audiopolicy.AudioMixingRule.AudioMixMatchCriterion criterion : criteria) {
                criterion.writeToParcel(dest);
            }
        }
    }

    private AudioPolicyConfig(android.os.Parcel in) {
        mMixes = new java.util.ArrayList<android.media.audiopolicy.AudioMix>();
        int nbMixes = in.readInt();
        for (int i = 0; i < nbMixes; i++) {
            final android.media.audiopolicy.AudioMix.Builder mixBuilder = new android.media.audiopolicy.AudioMix.Builder();
            // read mix route flags
            int routeFlags = in.readInt();
            mixBuilder.setRouteFlags(routeFlags);
            // read callback flags
            mixBuilder.setCallbackFlags(in.readInt());
            // read device information
            mixBuilder.setDevice(in.readInt(), in.readString());
            // read mix format
            int sampleRate = in.readInt();
            int encoding = in.readInt();
            int channelMask = in.readInt();
            final android.media.AudioFormat format = new android.media.AudioFormat.Builder().setSampleRate(sampleRate).setChannelMask(channelMask).setEncoding(encoding).build();
            mixBuilder.setFormat(format);
            // read mix rules
            int nbRules = in.readInt();
            android.media.audiopolicy.AudioMixingRule.Builder ruleBuilder = new android.media.audiopolicy.AudioMixingRule.Builder();
            for (int j = 0; j < nbRules; j++) {
                // read the matching rules
                ruleBuilder.addRuleFromParcel(in);
            }
            mixBuilder.setMixingRule(ruleBuilder.build());
            mMixes.add(mixBuilder.build());
        }
    }

    public static final android.os.Parcelable.Creator<android.media.audiopolicy.AudioPolicyConfig> CREATOR = new android.os.Parcelable.Creator<android.media.audiopolicy.AudioPolicyConfig>() {
        /**
         * Rebuilds an AudioPolicyConfig previously stored with writeToParcel().
         *
         * @param p
         * 		Parcel object to read the AudioPolicyConfig from
         * @return a new AudioPolicyConfig created from the data in the parcel
         */
        public android.media.audiopolicy.AudioPolicyConfig createFromParcel(android.os.Parcel p) {
            return new android.media.audiopolicy.AudioPolicyConfig(p);
        }

        public android.media.audiopolicy.AudioPolicyConfig[] newArray(int size) {
            return new android.media.audiopolicy.AudioPolicyConfig[size];
        }
    };

    public java.lang.String toLogFriendlyString() {
        java.lang.String textDump = new java.lang.String("android.media.audiopolicy.AudioPolicyConfig:\n");
        textDump += ((mMixes.size() + " AudioMix: ") + mRegistrationId) + "\n";
        for (android.media.audiopolicy.AudioMix mix : mMixes) {
            // write mix route flags
            textDump += ("* route flags=0x" + java.lang.Integer.toHexString(mix.getRouteFlags())) + "\n";
            // write mix format
            textDump += ("  rate=" + mix.getFormat().getSampleRate()) + "Hz\n";
            textDump += ("  encoding=" + mix.getFormat().getEncoding()) + "\n";
            textDump += "  channels=0x";
            textDump += java.lang.Integer.toHexString(mix.getFormat().getChannelMask()).toUpperCase() + "\n";
            // write mix rules
            final java.util.ArrayList<android.media.audiopolicy.AudioMixingRule.AudioMixMatchCriterion> criteria = mix.getRule().getCriteria();
            for (android.media.audiopolicy.AudioMixingRule.AudioMixMatchCriterion criterion : criteria) {
                switch (criterion.mRule) {
                    case android.media.audiopolicy.AudioMixingRule.RULE_EXCLUDE_ATTRIBUTE_USAGE :
                        textDump += "  exclude usage ";
                        textDump += criterion.mAttr.usageToString();
                        break;
                    case android.media.audiopolicy.AudioMixingRule.RULE_MATCH_ATTRIBUTE_USAGE :
                        textDump += "  match usage ";
                        textDump += criterion.mAttr.usageToString();
                        break;
                    case android.media.audiopolicy.AudioMixingRule.RULE_EXCLUDE_ATTRIBUTE_CAPTURE_PRESET :
                        textDump += "  exclude capture preset ";
                        textDump += criterion.mAttr.getCapturePreset();
                        break;
                    case android.media.audiopolicy.AudioMixingRule.RULE_MATCH_ATTRIBUTE_CAPTURE_PRESET :
                        textDump += "  match capture preset ";
                        textDump += criterion.mAttr.getCapturePreset();
                        break;
                    case android.media.audiopolicy.AudioMixingRule.RULE_MATCH_UID :
                        textDump += "  match UID ";
                        textDump += criterion.mIntProp;
                        break;
                    case android.media.audiopolicy.AudioMixingRule.RULE_EXCLUDE_UID :
                        textDump += "  exclude UID ";
                        textDump += criterion.mIntProp;
                        break;
                    default :
                        textDump += "invalid rule!";
                }
                textDump += "\n";
            }
        }
        return textDump;
    }

    protected void setRegistration(java.lang.String regId) {
        final boolean currentRegNull = (mRegistrationId == null) || mRegistrationId.isEmpty();
        final boolean newRegNull = (regId == null) || regId.isEmpty();
        if (((!currentRegNull) && (!newRegNull)) && (!mRegistrationId.equals(regId))) {
            android.util.Log.e(android.media.audiopolicy.AudioPolicyConfig.TAG, (("Invalid registration transition from " + mRegistrationId) + " to ") + regId);
            return;
        }
        mRegistrationId = (regId == null) ? "" : regId;
        int mixIndex = 0;
        for (android.media.audiopolicy.AudioMix mix : mMixes) {
            if (!mRegistrationId.isEmpty()) {
                if ((mix.getRouteFlags() & android.media.audiopolicy.AudioMix.ROUTE_FLAG_LOOP_BACK) == android.media.audiopolicy.AudioMix.ROUTE_FLAG_LOOP_BACK) {
                    mix.setRegistration((((mRegistrationId + "mix") + android.media.audiopolicy.AudioPolicyConfig.mixTypeId(mix.getMixType())) + ":") + (mixIndex++));
                } else
                    if ((mix.getRouteFlags() & android.media.audiopolicy.AudioMix.ROUTE_FLAG_RENDER) == android.media.audiopolicy.AudioMix.ROUTE_FLAG_RENDER) {
                        mix.setRegistration(mix.mDeviceAddress);
                    }

            } else {
                mix.setRegistration("");
            }
        }
    }

    private static java.lang.String mixTypeId(int type) {
        if (type == android.media.audiopolicy.AudioMix.MIX_TYPE_PLAYERS)
            return "p";
        else
            if (type == android.media.audiopolicy.AudioMix.MIX_TYPE_RECORDERS)
                return "r";
            else
                return "i";


    }

    protected java.lang.String getRegistration() {
        return mRegistrationId;
    }
}

