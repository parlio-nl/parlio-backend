package nl.parlio.ext.jooq.codegen;

import org.jooq.meta.Definition;

public class QPrefixGeneratorStrategy extends org.jooq.codegen.DefaultGeneratorStrategy {

  @Override
  public String getJavaClassName(Definition definition, Mode mode) {
    if (Mode.DEFAULT.equals(mode)) {
      return super.getJavaClassName(definition, mode) + "Table";
    }
    return "Q" + super.getJavaClassName(definition, mode);
  }
}
