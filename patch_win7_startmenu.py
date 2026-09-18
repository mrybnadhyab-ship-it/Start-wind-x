# Patch helper for the standalone Win7 Start Menu project.
# It intentionally does NOT modify OpenNavBar.
from pathlib import Path
p=Path("app/src/main/java/com/win7startmenu/StartMenuService.java")
s=p.read_text(encoding="utf-8")
print("Standalone Win7 Start Menu patch is present:", p.exists())
print("OpenNavBar is not modified by this project.")
