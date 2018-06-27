package com.stunstyle.imdb.finder.command.string;

import com.stunstyle.imdb.finder.exception.SeriesOrSeasonNotFoundException;
import com.stunstyle.imdb.finder.util.CommandParser;
import com.stunstyle.imdb.finder.util.FileManager;
import com.stunstyle.imdb.finder.util.JSONManager;

public class GetTvSeries implements StringCommand {
    private String command;

    public GetTvSeries(String command) {
        this.command = command;
    }

    @Override
    public String execute() {
        CommandParser commandParser = CommandParser.getCommandParser();
        String seriesName = commandParser.getTvSeriesName(command);
        int season = commandParser.getSeason(command);

        FileManager fm = FileManager.getFM();
        if (!fm.tvSeriesSeasonExistsInCache(seriesName, season)) {
            try {
                fm.downloadSeriesSeason(seriesName, season);
            } catch (SeriesOrSeasonNotFoundException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
                return e.getMessage();
            }
        }

        return JSONManager.getJSONManager().getEpisodesOfSeriesSeason(seriesName, season).replace("%20", " ");
    }

}
