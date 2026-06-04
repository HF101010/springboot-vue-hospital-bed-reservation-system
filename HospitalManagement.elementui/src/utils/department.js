const departmentMap = {
  Internal: "内科",
  Surgery: "外科",
  Pediatrics: "儿科",
  Obstetrics: "产科",
  Gynecology: "妇科",
  Emergency: "急诊科",
  ICU: "重症医学科",
  Oncology: "肿瘤科",
  Orthopedics: "骨科",
  Neurology: "神经内科",
  Neurosurgery: "神经外科",
  Cardiology: "心内科",
  Gastroenterology: "消化内科",
  Respiratory: "呼吸内科",
  Urology: "泌尿外科",
};

export function getDepartmentLabel(value) {
  if (value == null) return "";
  const text = String(value).trim();
  return departmentMap[text] || text;
}

export function toDepartmentOption(value) {
  return {
    label: getDepartmentLabel(value),
    value,
  };
}
