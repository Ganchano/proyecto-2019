package uy.edu.ucu.jsonql2019.ast;

import java.util.List;

/** Class for AST nodes of member expressions, e.g. `x[1]`, `x["a"]` or `x.a`.
 */
public class JSONQLMemberExpression extends JSONQLExpression {
	public final JSONQLExpression object;
	public final JSONQLExpression[] properties;
	
	public JSONQLMemberExpression(JSONQLExpression object, JSONQLExpression[] properties) {
		super();
		this.object = object;
		this.properties = properties;
	}

	public JSONQLMemberExpression(JSONQLExpression object, List<JSONQLExpression> properties) {
		this(object, properties.toArray(new JSONQLExpression[properties.size()]));
	}
	
	/** {@inheritDoc} */
	@Override public String unparse() {
		StringBuilder str = new StringBuilder();
		str.append(object.unparse()).append("[");
		unparse(str, properties);
		return str.append("]").toString();
	}

	/** {@inheritDoc} */
	@Override public String toJS() {
		return null; // TODO Auto-generated method stub
	}
}
