package Login;

public enum UserRole {
    DIRECTOR("Director"),
    GSO("General Support Officer"),
    HOD_XRAY("Head of X-Ray Dept"),
    HOD_EMERGENCY("Head of Emergency Dept"),
    HOD_LAB_BLOOD_BANK("Head of LAB & Blood Bank Dept"),
    HOD_OPD("Head of OPD Department"),
    HOD_INPATIENT("Head of Inpatient Dept"),
    HOD_BIN_QUTAB_COLLEGE("Head of Bin Qutab College of Health Sciences"),
    HOD_REHABILITATION("Head of Rehabilitation Dept"),
    HOD_EYE("Head of Eye Dept"),
    HOD_DENTAL("Head of Dental Dept"),
    HOD_DIALYSIS("Head of Dialysis Dept"),
    HOD_GENERAL_MAINTENANCE("Head of Hospital General Maintenance Dept"),
    HOD_SURGICAL("Head of Surgical Department");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

