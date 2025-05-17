package spotifum.musics.types;

import spotifum.musics.*;

import java.io.Serializable;

public class MultimediaMusic extends Music implements Serializable, ExplicitMusicInterface {
    private boolean explicit;
    private String video;

    public MultimediaMusic(Music music, boolean explicit,  String video) {
        super(music);
        this.explicit = explicit;
        this.video = video;
    }

    public boolean equals(Object o) {
        if  (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MultimediaMusic that = (MultimediaMusic) o;
        return that.equals(this);
    }
}
