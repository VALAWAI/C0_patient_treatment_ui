export const environment = {
	production: true,
	componentUrl: (window as { [key: string]: any })["env"]["componentUrl"] || "http://co_patient_treatment_ui.valawai.eu"
};
