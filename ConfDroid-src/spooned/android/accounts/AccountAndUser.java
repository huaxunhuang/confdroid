/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.accounts;


/**
 * Used to store the Account and the UserId this account is associated with.
 *
 * @unknown 
 */
public class AccountAndUser {
    public android.accounts.Account account;

    public int userId;

    public AccountAndUser(android.accounts.Account account, int userId) {
        this.account = account;
        this.userId = userId;
    }

    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if (!(o instanceof android.accounts.AccountAndUser))
            return false;

        final android.accounts.AccountAndUser other = ((android.accounts.AccountAndUser) (o));
        return this.account.equals(other.account) && (this.userId == other.userId);
    }

    @java.lang.Override
    public int hashCode() {
        return account.hashCode() + userId;
    }

    public java.lang.String toString() {
        return (account.toString() + " u") + userId;
    }
}

