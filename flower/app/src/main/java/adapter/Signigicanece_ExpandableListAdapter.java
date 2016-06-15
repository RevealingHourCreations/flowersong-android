package adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import SetterGetter.Significances;
import in.flowersong.Flow_desc;
import in.flowersong.R;
import util.Typefaces;

//For expandable list view use BaseExpandableListAdapter
public class Signigicanece_ExpandableListAdapter extends BaseExpandableListAdapter implements SectionIndexer {
    private Context _context;
    private List<String> header; // _significance_row titles
    // Child data in format of _significance_row title, child title
    private HashMap<String, List<Significances>> child;
    private Character[] mSectionLetters;
    private int[] mSectionIndices;
    private ArrayList<String> mData;
    public Signigicanece_ExpandableListAdapter(Context context, List<String> listDataHeader,
                                               HashMap<String, List<Significances>> listChildData) {
        this.mData = (ArrayList<String>) listDataHeader;
        this._context = context;
        this.header = listDataHeader;
        this.child = listChildData;
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionLetters();
    }
    private int[] getSectionIndices() {
        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
        char lastFirstChar = mData.get(0).charAt(0);
        sectionIndices.add(0);
        for (int i = 1; i < mData.size(); i++) {
            if (mData.get(i).charAt(0) != lastFirstChar) {
                lastFirstChar = mData.get(i).charAt(0);
                sectionIndices.add(i);
            }
        }
        int[] sections = new int[sectionIndices.size()];
        for (int i = 0; i < sectionIndices.size(); i++) {
            sections[i] = sectionIndices.get(i);
        }
        return sections;
    }

    private Character[] getSectionLetters() {
        Character[] letters = new Character[mSectionIndices.length];
        for (int i = 0; i < mSectionIndices.length; i++) {
            letters[i] = mData.get(mSectionIndices[i]).charAt(0);

        }
        return letters;
    }
    @Override
    public Object getChild(int groupPosition, int childPosititon) {

        // This will return the child
        return this.child.get(this.header.get(groupPosition)).get(
                childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        // Getting child text
        final Significances childText = (Significances) getChild(groupPosition, childPosition);

        // Inflating child layout and setting textview
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.childs, parent, false);
        }

        TextView child_text = (TextView) convertView.findViewById(R.id.child);
        try {
            if (Typefaces.get(_context, "megmaIIW04_semibold") != null) {
                child_text.setTypeface(Typefaces.get(_context, "megmaIIW04_semibold"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        child_text.setTag(childText.getUrl().toString());
        child_text.setText(childText.getSignificance());

        child_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final Significances childText = (Significances) getChild(groupPosition, childPosition);
                    // Toast.makeText(_context,childText.getUrl(),Toast.LENGTH_SHORT).show();

                    if (childText != null) {
                        Intent i = new Intent(view.getContext(), Flow_desc.class);
                        i.putExtra("ACIONBARNAME", childText.getName().toString());
                        i.putExtra("URLNAME", childText.getUrl());
                        i.putExtra("FROMSIGNI", true);
                        view.getContext().startActivity(i);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        // return children count
        return this.child.get(this.header.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {

        // Get _significance_row position
        return this.header.get(groupPosition);
    }

    @Override
    public int getGroupCount() {

        // Get _significance_row size
        return this.header.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        // Getting _significance_row title
        String headerTitle = (String) getGroup(groupPosition);

        // Inflating _significance_row layout and setting text
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout._significance_row, parent, false);
        }

        TextView header_text = (TextView) convertView.findViewById(R.id.header);
        try {
            if (Typefaces.get(_context, "megmaIIW04_semibold") != null) {
                header_text.setTypeface(Typefaces.get(_context, "megmaIIW04_semibold"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        header_text.setText(headerTitle);

        // If group is expanded then change the text into bold and change the
        // icon
//        if (isExpanded) {
//
//            header_text.setTypeface(null, Typeface.BOLD);
//            header_text.setCompoundDrawablesWithIntrinsicBounds(0, 0,
//                    R.drawable.ic_keyboard_arrow_up_black_24dp, 0);
//        } else {
//            // If group is not expanded then change the text back into normal
//            // and change the icon
//
//            header_text.setTypeface(null, Typeface.NORMAL);
//            header_text.setCompoundDrawablesWithIntrinsicBounds(0, 0,
//                    R.drawable.ic_keyboard_arrow_down_black_24dp, 0);
//        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    @Override
    public int getPositionForSection(int section) {
        if (mSectionIndices.length == 0) {
            return 0;
        }

        if (section >= mSectionIndices.length) {
            section = mSectionIndices.length - 1;
        } else if (section < 0) {
            section = 0;
        }
        return mSectionIndices[section];
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < mSectionIndices.length; i++) {
            if (position < mSectionIndices[i]) {
                return i - 1;
            }
        }
        return mSectionIndices.length - 1;
    }

    @Override
    public Object[] getSections() {
        return mSectionLetters;
    }



}