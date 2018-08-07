/*
 * Copyright (C) 2014  Ian Holsman
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package utl;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.UserPrincipal;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.Set;

/**
 * Created by iholsman on 5/30/2017.
 * All Rights reserved
 */
public class FileIO {
    public static boolean isInSecureDir(Path file) {
        return isInSecureDir(file, null);
    }
    public static boolean isInSecureDir(Path file, UserPrincipal user) {
        return isInSecureDir(file, user, 5);
    }

    /**
     * Indicates whether file lives in a secure directory relative
     * to the program's user
     * @param file Path to test
     * @param user User to test. If null, defaults to current user
     * @param symlinkDepth Number of symbolic links allowed
     * @return true if file's directory is secure.
     */
    public static boolean isInSecureDir(Path file, UserPrincipal user,
                                        int symlinkDepth) {
        if (!file.isAbsolute()) {
            file = file.toAbsolutePath();
        } if (symlinkDepth <=0) {
            // Too many levels of symbolic links
            return false;
        }

        // Get UserPrincipal for specified user and superuser
        FileSystem fileSystem =
                Paths.get(file.getRoot().toString()).getFileSystem();
        UserPrincipalLookupService upls =
                fileSystem.getUserPrincipalLookupService();
        UserPrincipal root = null;
        try {
            root = upls.lookupPrincipalByName("root");
            if (user == null) {
                user = upls.lookupPrincipalByName(System.getProperty("user.name"));
            }
            if (root == null || user == null) {
                return false;
            }
        } catch (IOException x) {
            return false;
        }

        // If any parent dirs (from root on down) are not secure,
        // dir is not secure
        for (int i = 1; i <= file.getNameCount(); i++) {
            Path partialPath = Paths.get(file.getRoot().toString(),
                    file.subpath(0, i).toString());

            try {
                if (Files.isSymbolicLink(partialPath)) {
                    if (!isInSecureDir(Files.readSymbolicLink(partialPath), user, symlinkDepth -1)) {
                        // Symbolic link, linked-to dir not secure
                        return false;
                    }
                } else {
                    UserPrincipal owner = Files.getOwner(partialPath);
                    if (!user.equals(owner) && !root.equals(owner)) {
                        // dir owned by someone else, not secure
                        return false;
                    }
                    PosixFileAttributes attr =
                            Files.readAttributes(partialPath, PosixFileAttributes.class);
                    Set<PosixFilePermission> perms = attr.permissions();
                    if (perms.contains(PosixFilePermission.GROUP_WRITE) ||
                            perms.contains(PosixFilePermission.OTHERS_WRITE)) {
                        // Someone else can write files, not secure
                        return false;
                    }
                }
            } catch (IOException x) {
                return false;
            }
        }

        return true;
    }
}
