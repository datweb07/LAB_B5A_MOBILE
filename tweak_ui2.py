import re

# Fix activity_main.xml (Toolbar popup theme)
path_xml = 'app/src/main/res/layout/activity_main.xml'
with open(path_xml, 'r', encoding='utf-8') as f:
    xml_content = f.read()

# Add popupTheme after app:theme
if 'app:popupTheme' not in xml_content:
    xml_content = xml_content.replace('app:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar"', 'app:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar"\n        app:popupTheme="@style/ThemeOverlay.AppCompat.Light"')

with open(path_xml, 'w', encoding='utf-8') as f:
    f.write(xml_content)


# Fix dialog_new_student.xml (NumberPicker size and gravity)
path_dialog = 'app/src/main/res/layout/dialog_new_student.xml'
with open(path_dialog, 'r', encoding='utf-8') as f:
    dialog_content = f.read()

old_np = '''<NumberPicker
        android:id="@+id/np_student_age"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="8dp" />'''

new_np = '''<NumberPicker
        android:id="@+id/np_student_age"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center_horizontal"
        android:layout_marginTop="8dp" />'''

dialog_content = dialog_content.replace(old_np, new_np)

with open(path_dialog, 'w', encoding='utf-8') as f:
    f.write(dialog_content)

print("Tweaks applied successfully")