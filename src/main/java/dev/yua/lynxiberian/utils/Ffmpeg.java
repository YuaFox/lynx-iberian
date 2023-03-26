package dev.yua.lynxiberian.utils;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class Ffmpeg {

    public File merge(File video, File audio) {
        String outputPath = video.getParent() + "/MERGED_" + video.getName();
        String[] exeCmd = new String[]
                {"ffmpeg", "-i", audio.getPath(), "-i", video.getPath() ,"-acodec", "copy", "-vcodec", "copy", outputPath};

        ProcessBuilder pb = new ProcessBuilder(exeCmd);
        Process p = null;
        try {
            p = pb.start();
            boolean finished = p.waitFor(30, TimeUnit.SECONDS);
            if(finished) {
                return new File(outputPath);
            }else{
                System.err.println("Ffmpeg cant merge video");
                p.destroy();
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("oops");
            if(p != null)
                p.destroy();
            return null;
        }
    }
}