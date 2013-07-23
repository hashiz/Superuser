package com.koushikdutta.superuser.util;

import java.io.File;

public class TempUnroot {
	private static final String FAKE_SU_BINARY = "/system/bacon";
	private static final String TRUE_SU_BINARY = "/system/xbin/su";
	private static final String TRUE_SU_LINK   = "/system/bin/su";

	public static boolean doTempUnroot() {
      	boolean success = false;

      	if (! new File(TRUE_SU_BINARY).exists() ) {
        	return false;
        }

        try {
            final String command =
                    "mount -orw,remount /system\n" +
                    String.format("rm %s\n",    FAKE_SU_BINARY) +
                    String.format("ln %s %s\n", TRUE_SU_BINARY, FAKE_SU_BINARY) +
                    String.format("rm %s\n",    TRUE_SU_BINARY) +
                    String.format("rm %s\n",    TRUE_SU_LINK) +
                    "mount -oro,remount /system\n" +
                    "sync\n";
            Process p = Runtime.getRuntime().exec("su");
            p.getOutputStream().write(command.getBytes());
            p.getOutputStream().close();
            if (p.waitFor() != 0)
                throw new Exception("non zero result");
            success = true;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return success;
    }

	public static boolean doTempRooting() {
        boolean success = false;

        if (! new File(FAKE_SU_BINARY).exists() ) {
        	return success;
        }

        try {
            final String command =
                    "mount -orw,remount /system\n" +
                    String.format("rm %s\n",    TRUE_SU_BINARY) +
                    String.format("rm %s\n",    TRUE_SU_LINK) +
                    String.format("ln %s %s\n", FAKE_SU_BINARY, TRUE_SU_BINARY) +
                    String.format("ln -s %s %s\n", TRUE_SU_BINARY, TRUE_SU_LINK) +
                    String.format("rm %s\n",    FAKE_SU_BINARY) +
                    "mount -oro,remount /system\n" +
                    "sync\n";
            Process p = Runtime.getRuntime().exec(FAKE_SU_BINARY);
            p.getOutputStream().write(command.getBytes());
            p.getOutputStream().close();
            if (p.waitFor() != 0)
                throw new Exception("non zero result");
            success = true;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return success;
    }
}
