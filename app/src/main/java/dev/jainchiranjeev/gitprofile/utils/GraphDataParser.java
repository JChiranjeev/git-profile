package dev.jainchiranjeev.gitprofile.utils;

import android.content.Context;
import android.graphics.Color;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dev.jainchiranjeev.gitprofile.R;
import dev.jainchiranjeev.gitprofile.models.GitRepoModel;

public class GraphDataParser {
    Context context;

    public GraphDataParser(Context context) {
        this.context = context;
    }

    public PieDataSet getPieData(List<GitRepoModel> modelList) {
        List<PieEntry> pieDataList = new ArrayList<>();
        PieDataSet pieDataSet = null;
        HashMap<String, Integer> map = getLanguagesMap(modelList);
        Iterator it = map.entrySet().iterator();
//        Iterate over HashMap and create ArrayList of PieEntry
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            pieDataList.add(new PieEntry(Float.parseFloat(pair.getValue().toString()), pair.getKey().toString()));
//            Avoids Concurrent Modification Exception
            it.remove();
        }
        pieDataSet = new PieDataSet(pieDataList, "");
//        Set color palette for Pie Graph
        pieDataSet.setColors(getColorList());
        return pieDataSet;
    }

    public BarDataSet getBarData(List<GitRepoModel> modelList) {
        BarDataSet barDataSet = null;
//        Sort the modelList according to stargazers count
        Collections.sort(modelList, new Comparator<GitRepoModel>() {
            @Override
            public int compare(GitRepoModel t0, GitRepoModel t1) {
                return t1.stargazersCount.compareTo(t0.stargazersCount);
            }
        });
        List<BarEntry> barDataList = new ArrayList<>();
        List<String> labelsList = new ArrayList<>();
        for (int i=0; i<getBarGraphLimit(modelList); i++) {
            GitRepoModel model = modelList.get(i);
            barDataList.add(new BarEntry(modelList.indexOf(model),model.stargazersCount));
            labelsList.add(model.name);
        }
        barDataSet = new BarDataSet(barDataList, "");
//        Set color palette for Bar Graph
        barDataSet.setColors(getColorList());
        return barDataSet;
    }

    public int getBarGraphLimit(List<GitRepoModel> modelList) {
        return Math.min(modelList.size(), 9);
    }

    public List<Integer> getColorList() {
        List<Integer> colorList = new ArrayList<>();
        colorList.add(ContextCompat.getColor(context, R.color.md_blue_grey_900));
        colorList.add(ContextCompat.getColor(context, R.color.md_blue_grey_800));
        colorList.add(ContextCompat.getColor(context, R.color.md_blue_grey_700));
        colorList.add(ContextCompat.getColor(context, R.color.md_blue_grey_600));
        colorList.add(ContextCompat.getColor(context, R.color.md_blue_grey_500));
        colorList.add(ContextCompat.getColor(context, R.color.md_blue_grey_400));
        colorList.add(ContextCompat.getColor(context, R.color.md_blue_grey_300));
        colorList.add(ContextCompat.getColor(context, R.color.md_blue_grey_200));
        colorList.add(ContextCompat.getColor(context, R.color.md_blue_grey_100));
        return colorList;
    }

    public List<LegendEntry> getTopReposNames(List<GitRepoModel> modelList) {
        List<String> reposNameList = new ArrayList<>();
        List<LegendEntry> entryList = new ArrayList<>();
        List<Integer> colorList = getColorList();
        Collections.sort(modelList, new Comparator<GitRepoModel>() {
            @Override
            public int compare(GitRepoModel t0, GitRepoModel t1) {
                return t1.stargazersCount.compareTo(t0.stargazersCount);
            }
        });

        for (int i=0; i<getBarGraphLimit(modelList); i++) {
            GitRepoModel model = modelList.get(i);
            reposNameList.add(model.name);
            LegendEntry entry = new LegendEntry();
            entry.label = model.name;
            entry.formColor = colorList.get(i % getBarGraphLimit(modelList));
            entryList.add(entry);
        }

        return entryList;
    }

    private HashMap<String, Integer> getLanguagesMap(List<GitRepoModel> modelList) {
        HashMap<String, Integer> langMap = new HashMap<String, Integer>();

        for (GitRepoModel model : modelList) {
            if (langMap.containsKey(model.language)) {
//                Increment counter if key already exists
                int count = langMap.get(model.language);
                langMap.put(model.language, count + 1);
            } else {
//                Create new entry in HashMap for new key
                langMap.put(model.language, 1);
            }
        }
//        Replace null key with 'Others'
        if (langMap.containsKey("null")) {
            int count = langMap.get("null");
            langMap.remove("null");
            langMap.put("Others", count);
        }

        return langMap;
    }
}
