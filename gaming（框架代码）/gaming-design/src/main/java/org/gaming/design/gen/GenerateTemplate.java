/**
 * 
 */
package org.gaming.design.gen;

import java.util.ArrayList;
import java.util.List;

import org.gaming.design.loader.IDesignReader;
import org.gaming.design.meta.DesignClassMeta;
import org.gaming.design.meta.DesignCollectorMeta;
import org.gaming.design.meta.DesignColumnMeta;
import org.gaming.design.meta.DesignIndexMeta;
import org.gaming.design.meta.ExportType;

/**
 * @author YY
 *
 */
public abstract class GenerateTemplate {
	
	protected List<GeneratePart> parts = new ArrayList<>();
	
	protected abstract String mapDeclare();
	protected abstract String listDeclare(String elementType);
	public abstract void gen(String outputPath, String exportSourcePath, IDesignReader designReader, ExportType exportType);
	
	protected boolean isExport(DesignClassMeta classMeta, ExportType exportType) {
		return classMeta.getExportType() == ExportType.BOTH || classMeta.getExportType() == exportType;
	}
	
	protected String formatIndexStruct(DesignClassMeta classMeta, DesignIndexMeta indexMeta, int index) {
		if (index >= indexMeta.getFields().size() - 1) {
			String keyName = indexMeta.getFields().get(index);
			DesignColumnMeta keyMeta = classMeta.nameToColumns().get(keyName);
			String typeExplain = this.typeExplain(keyMeta.getType());
			return this.mapDeclare().replace("#key#", typeExplain).replace("#value#", formatClassName(classMeta));
		} else {
			String keyName = indexMeta.getFields().get(index);
			DesignColumnMeta keyMeta = classMeta.nameToColumns().get(keyName);
			String typeExplain = this.typeExplain(keyMeta.getType());
			return this.mapDeclare().replace("#key#", typeExplain).replace("#value#", formatIndexStruct(classMeta, indexMeta, index + 1));
		}
	}
	
	protected final String formatIndexSubStruct(DesignClassMeta classMeta, DesignIndexMeta indexMeta, int indexLayer) {
		return formatIndexStruct(classMeta, indexMeta, indexLayer);
	}
	
	protected String formatCollectorStruct(DesignClassMeta classMeta, DesignCollectorMeta collectorMeta, int index) {
		if (index >= collectorMeta.getFields().size() - 1) {
			String keyName = collectorMeta.getFields().get(index);
			DesignColumnMeta keyMeta = classMeta.nameToColumns().get(keyName);
			String typeExplain = this.typeExplain(keyMeta.getType());
			return this.mapDeclare().replace("#key#", typeExplain).replace("#value#", this.listDeclare(formatClassName(classMeta)));
		} else {
			String keyName = collectorMeta.getFields().get(index);
			DesignColumnMeta keyMeta = classMeta.nameToColumns().get(keyName);
			String typeExplain = this.typeExplain(keyMeta.getType());
			return this.mapDeclare().replace("#key#", typeExplain).replace("#value#", formatCollectorStruct(classMeta, collectorMeta, index + 1));
		}
	}
	
	protected final String formatCollectorSubStruct(DesignClassMeta classMeta, DesignCollectorMeta collectorMeta, int indexLayer) {
		return formatCollectorStruct(classMeta, collectorMeta, indexLayer);
	}
	
	
	
	public final List<String> genIndexMethods(DesignClassMeta classMeta) {
		List<String> result = new ArrayList<>(parts.size());
		for(GeneratePart part : parts) {
			result.add(genIndexMethod(classMeta, part));
		}
		return result;
	}
	
	protected abstract String genIndexMethod(DesignClassMeta classMeta, GeneratePart part);
	
	public final List<String> genCollectorMethods(DesignClassMeta classMeta) {
		List<String> result = new ArrayList<>(parts.size());
		for(GeneratePart part : parts) {
			result.add(genCollectorMethod(classMeta, part));
		}
		return result;
	}
	
	protected abstract String genCollectorMethod(DesignClassMeta classMeta, GeneratePart part);
	
	//类型解析
	protected abstract String typeExplain(String typeName);
	
	protected abstract String genIndexFill(DesignClassMeta classMeta);
	protected abstract String genCollectorFill(DesignClassMeta classMeta);
	
	public abstract String formatClassName(DesignClassMeta classMeta);
}
