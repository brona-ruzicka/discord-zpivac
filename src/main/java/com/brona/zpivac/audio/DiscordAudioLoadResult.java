package com.brona.zpivac.audio;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.List;

public class DiscordAudioLoadResult {

    protected final String identifier;

    protected final boolean empty;
    protected final boolean search;
    protected final List<AudioTrack> list;

    public DiscordAudioLoadResult(String identifier, boolean empty, boolean search, List<AudioTrack> list) {
        this.identifier = identifier;
        this.empty = empty;
        this.search = search;
        this.list = list;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public boolean isSearch() {
        return this.search;
    }

    public List<AudioTrack> getList() {
        return this.list;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof DiscordAudioLoadResult)) return false;
        final DiscordAudioLoadResult other = (DiscordAudioLoadResult) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$identifier = this.getIdentifier();
        final Object other$identifier = other.getIdentifier();
        if (this$identifier == null ? other$identifier != null : !this$identifier.equals(other$identifier))
            return false;
        if (this.isEmpty() != other.isEmpty()) return false;
        if (this.isSearch() != other.isSearch()) return false;
        final Object this$list = this.getList();
        final Object other$list = other.getList();
        if (this$list == null ? other$list != null : !this$list.equals(other$list)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof DiscordAudioLoadResult;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $identifier = this.getIdentifier();
        result = result * PRIME + ($identifier == null ? 43 : $identifier.hashCode());
        result = result * PRIME + (this.isEmpty() ? 79 : 97);
        result = result * PRIME + (this.isSearch() ? 79 : 97);
        final Object $list = this.getList();
        result = result * PRIME + ($list == null ? 43 : $list.hashCode());
        return result;
    }

    public String toString() {
        return "DiscordAudioLoadResult(identifier=" + this.getIdentifier() + ", empty=" + this.isEmpty() + ", search=" + this.isSearch() + ", list=" + this.getList() + ")";
    }
}
